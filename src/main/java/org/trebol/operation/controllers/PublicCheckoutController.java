/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.integration.exceptions.MailingServiceException;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.crud.ISalesCrudService;
import org.trebol.operation.ICheckoutService;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@RestController
@RequestMapping("/public/checkout")
public class PublicCheckoutController {
  private static final String WEBPAY_SUCCESS_TOKEN_HEADER_NAME = "token_ws";
  private static final String WEBPAY_ABORTION_TOKEN_HEADER_NAME = "TBK_TOKEN";
  private final Logger logger = LoggerFactory.getLogger(PublicCheckoutController.class);
  private final ICheckoutService service;
  private final ISalesCrudService salesCrudService;
  private final IPredicateJpaService<Sell> salesPredicateService;
  @Nullable
  private final IMailingIntegrationService mailingIntegrationService;

  @Autowired
  public PublicCheckoutController(
    ICheckoutService service,
    ISalesCrudService salesCrudService,
    IPredicateJpaService<Sell> salesPredicateService,
    @Autowired(required = false) IMailingIntegrationService mailingIntegrationService
  ) {
    this.service = service;
    this.salesCrudService = salesCrudService;
    this.salesPredicateService = salesPredicateService;
    this.mailingIntegrationService = mailingIntegrationService;
  }

  /**
   * Save a new transaction, forward request to checkout server, and save the generated token for later validation
   *
   * @param transactionRequest The checkout details
   * @return An object wrapping the URL and token to redirect the user with, towards their payment page.
   * @throws org.trebol.exceptions.BadInputException                   If the input pojo class contains invalid data
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during the payment
   *                                                                   integration process
   */
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('checkout')")
  public PaymentRedirectionDetailsPojo submitCart(@Valid @RequestBody SellPojo transactionRequest)
    throws BadInputException, PaymentServiceException, EntityExistsException {
    SellPojo createdTransaction = salesCrudService.create(transactionRequest);
    return service.requestTransactionStart(createdTransaction);
  }

  /**
   * Validate token sent from WebPay Plus after a succesful transaction
   *
   * @param transactionData The HTTP headers
   * @return A 303 SEE OTHER response
   * @throws org.trebol.exceptions.BadInputException                   If the expected token is not present in the request
   * @throws EntityNotFoundException                                   If the token does not match that of any "pending" transaction
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during internal API calls
   */
  @GetMapping({"/validate", "/validate/"})
  public ResponseEntity<Void> validateSuccesfulTransaction(@RequestParam Map<String, String> transactionData)
    throws BadInputException, EntityNotFoundException, PaymentServiceException, MailingServiceException {
    if (!transactionData.containsKey(WEBPAY_SUCCESS_TOKEN_HEADER_NAME)) { // success
      throw new BadInputException("No transaction token was provided");
    }
    String token = transactionData.get(WEBPAY_SUCCESS_TOKEN_HEADER_NAME);
    SellPojo sellPojo = service.confirmTransaction(token, false);
    if (this.mailingIntegrationService != null) {
      mailingIntegrationService.notifyOrderStatusToClient(sellPojo);
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
   * @throws org.trebol.exceptions.BadInputException                   If the expected token is not present in the request
   * @throws EntityNotFoundException                                   If the token does not match that of any "pending" transaction
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during internal API calls
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
