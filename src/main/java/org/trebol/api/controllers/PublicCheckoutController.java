/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.OrderPojo;
import org.trebol.api.services.CheckoutService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.crud.OrdersCrudService;
import org.trebol.jpa.services.predicates.OrdersPredicateService;
import org.trebol.mailing.MailingService;
import org.trebol.mailing.MailingServiceException;
import org.trebol.payment.PaymentServiceException;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;
import static org.trebol.config.Constants.WEBPAY_ABORTION_TOKEN_HEADER_NAME;
import static org.trebol.config.Constants.WEBPAY_SUCCESS_TOKEN_HEADER_NAME;

@RestController
@RequestMapping("/public/checkout")
@Tag(name = "Checkout")
public class PublicCheckoutController {
    private final CheckoutService service;
    private final OrdersCrudService ordersCrudService;
    private final OrdersPredicateService ordersPredicateService;
    @Nullable
    private final MailingService mailingService;

    @Autowired
    public PublicCheckoutController(
        CheckoutService service,
        OrdersCrudService ordersCrudService,
        OrdersPredicateService ordersPredicateService,
        @Autowired(required = false) MailingService mailingService
    ) {
        this.service = service;
        this.ordersCrudService = ordersCrudService;
        this.ordersPredicateService = ordersPredicateService;
        this.mailingService = mailingService;
    }

    /**
     * Save a new transaction, forward request to checkout server, and save the generated token for later validation
     *
     * @param transactionRequest The checkout details
     * @return An object wrapping the URL and token to redirect the user with, towards their payment page.
     * @throws BadInputException       If the input models class contains invalid data
     * @throws PaymentServiceException If an error happens during the payment
     *                                 payment process
     */
    @PostMapping
    @Operation(summary = "Submit cart contents to request an order and begin a checkout")
    @PreAuthorize("hasAuthority('" + AUTHORITY_CHECKOUT + "')")
    public PaymentRedirectionDetailsPojo submitCart(@Valid @RequestBody OrderPojo transactionRequest)
        throws BadInputException, PaymentServiceException, EntityExistsException {
        OrderPojo createdTransaction = ordersCrudService.create(transactionRequest);
        return service.requestTransactionStart(createdTransaction);
    }

    /**
     * Validate token sent from WebPay Plus after a succesful transaction
     *
     * @param transactionData The HTTP headers
     * @return A 303 SEE OTHER response
     * @throws BadInputException       If the expected token is not present in the request
     * @throws EntityNotFoundException If the token does not match that of any "pending" transaction
     * @throws PaymentServiceException If an error happens during internal API calls
     */
    @GetMapping("/validate")
    @Operation(summary = "Request that an order status be updated after having begun checkout")
    public ResponseEntity<Void> validateSuccesfulTransaction(@RequestParam Map<String, String> transactionData)
        throws BadInputException, EntityNotFoundException, PaymentServiceException, MailingServiceException {
        if (!transactionData.containsKey(WEBPAY_SUCCESS_TOKEN_HEADER_NAME)) { // success
            throw new BadInputException("No transaction token was provided");
        }
        String token = transactionData.get(WEBPAY_SUCCESS_TOKEN_HEADER_NAME);
        OrderPojo orderPojo = service.confirmTransaction(token, false);
        if (this.mailingService!=null) {
            mailingService.notifyOrderStatusToClient(orderPojo);
        }
        URI transactionUri = service.generateResultPageUrl(token);
        return ResponseEntity
            .status(SEE_OTHER)
            .location(transactionUri)
            .build();
    }

    /**
     * Validate token sent from WebPay Plus after a transaction was aborted
     *
     * @param transactionData The HTTP headers
     * @return A 303 SEE OTHER response
     * @throws BadInputException       If the expected token is not present in the request
     * @throws EntityNotFoundException If the token does not match that of any "pending" transaction
     * @throws PaymentServiceException If an error happens during internal API calls
     */
    @PostMapping("/validate")
    @Operation(summary = "Submit failed or aborted state for an order after having begun checkout")
    public ResponseEntity<Void> validateAbortedTransaction(@RequestParam Map<String, String> transactionData)
        throws BadInputException, EntityNotFoundException, PaymentServiceException {

        if (!transactionData.containsKey(WEBPAY_ABORTION_TOKEN_HEADER_NAME)) { // aborted
            throw new BadInputException("No transaction token was provided");
        }
        String token = transactionData.get(WEBPAY_ABORTION_TOKEN_HEADER_NAME);
        service.confirmTransaction(token, true);
        URI resultPageUrl = service.generateResultPageUrl(token);
        return ResponseEntity
            .status(SEE_OTHER)
            .location(resultPageUrl)
            .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PaymentServiceException.class)
    public String handleException(PaymentServiceException ex) {
        return ex.getMessage();
    }
}
