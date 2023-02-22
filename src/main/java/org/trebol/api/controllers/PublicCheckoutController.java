/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.api.services.CheckoutService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.jpa.services.predicates.SalesPredicateService;
import org.trebol.mailing.MailingService;
import org.trebol.mailing.MailingServiceException;
import org.trebol.payment.PaymentServiceException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.trebol.config.Constants.*;

@RestController
@RequestMapping("/public/checkout")
public class PublicCheckoutController {
  private final CheckoutService service;
  private final SalesCrudService salesCrudService;
  private final SalesPredicateService salesPredicateService;
  @Nullable
  private final MailingService mailingService;

  @Autowired
  public PublicCheckoutController(
    CheckoutService service,
    SalesCrudService salesCrudService,
    SalesPredicateService salesPredicateService,
    @Autowired(required = false) MailingService mailingService
  ) {
    this.service = service;
    this.salesCrudService = salesCrudService;
    this.salesPredicateService = salesPredicateService;
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
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('" + AUTHORITY_CHECKOUT + "')")
  public PaymentRedirectionDetailsPojo submitCart(@Valid @RequestBody SellPojo transactionRequest)
    throws BadInputException, PaymentServiceException, EntityExistsException {
    // TODO this is the root of all evil. "creating a sale" here definitely sounds wrong - while "creating an order" does not.
    SellPojo createdTransaction = salesCrudService.create(transactionRequest);
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
  @GetMapping({"/validate", "/validate/"})
  public ResponseEntity<Void> validateSuccesfulTransaction(@RequestParam Map<String, String> transactionData)
    throws BadInputException, EntityNotFoundException, PaymentServiceException, MailingServiceException {
    if (!transactionData.containsKey(WEBPAY_SUCCESS_TOKEN_HEADER_NAME)) { // success
      throw new BadInputException("No transaction token was provided");
    }
    String token = transactionData.get(WEBPAY_SUCCESS_TOKEN_HEADER_NAME);
    SellPojo sellPojo = service.confirmTransaction(token, false);
    if (this.mailingService != null) {
      mailingService.notifyOrderStatusToClient(sellPojo);
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
  @PostMapping({"/validate", "/validate/"})
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

  /**
   * Fetch result of transaction after it has been confirmed and validated
   *
   * @param token The token used during the transaction
   * @return An object with all available data about the transaction
   * @throws EntityNotFoundException when no transaction matched the provided token
   */
  @GetMapping({"/result/{token}", "/result/{token}/"})
  public SellPojo getTransactionResultFor(@NotBlank @PathVariable String token)
    throws EntityNotFoundException {
    Map<String, String> tokenMatcher = Maps.of("token", token).build();
    Predicate withMatchingToken = salesPredicateService.parseMap(tokenMatcher);
    return salesCrudService.readOne(withMatchingToken);
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(PaymentServiceException.class)
  public String handleException(PaymentServiceException ex) {
    return ex.getMessage();
  }
}
