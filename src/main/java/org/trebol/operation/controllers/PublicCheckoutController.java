package org.trebol.operation.controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.operation.ICheckoutService;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SEE_OTHER;


/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/public/checkout")
public class PublicCheckoutController {

  private final ICheckoutService service;
  private static final String WEBPAY_SUCCESS_TOKEN_HEADER_NAME = "token_ws";
  private static final String WEBPAY_ABORTION_TOKEN_HEADER_NAME = "TBK_TOKEN";

  @Autowired
  public PublicCheckoutController(ICheckoutService service) {
    this.service = service;
  }

  /**
   * Save a new transaction, forward request to checkout server, and save the generated token for later validation
   *
   * @param transactionRequest The checkout details
   * @return An object wrapping the URL and token to redirect the user with, towards their payment page.
   * @throws org.trebol.exceptions.BadInputException If the input pojo class contains invalid data
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during the payment
   *                                                                   integration process
   */
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('checkout')")
  public PaymentRedirectionDetailsPojo submitCart(@Valid @RequestBody SellPojo transactionRequest)
    throws BadInputException, PaymentServiceException {
    SellPojo createdTransaction = service.saveCartAsPendingTransaction(transactionRequest);
    return service.requestTransactionStart(createdTransaction);
  }

  /**
   * Validate token sent from WebPay Plus after a succesful transaction.
   *
   * @param transactionData The HTTP headers
   * @return A 303 SEE OTHER response
   * @throws org.trebol.exceptions.BadInputException If the expected token is not present in the request
   * @throws javassist.NotFoundException If the token does not match that of any "pending" transaction
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during internal API calls
   */
  @GetMapping({"/validate", "/validate/"})
  public ResponseEntity<Void> validateSuccesfulTransaction(@RequestParam Map<String, String> transactionData)
      throws BadInputException, NotFoundException, PaymentServiceException {
    if (!transactionData.containsKey(WEBPAY_SUCCESS_TOKEN_HEADER_NAME)) { // success
      throw new BadInputException("No transaction token was provided");
    }
    String token = transactionData.get(WEBPAY_SUCCESS_TOKEN_HEADER_NAME);
    URI transactionUri = service.confirmTransaction(token, false);
    return ResponseEntity
        .status(SEE_OTHER)
        .location(transactionUri)
        .build();
  }

  /**
   * Validate token sent from WebPay Plus after a transaction was aborted.
   *
   * @param transactionData The HTTP headers
   * @return A 303 SEE OTHER response
   * @throws org.trebol.exceptions.BadInputException If the expected token is not present in the request
   * @throws javassist.NotFoundException If the token does not match that of any "pending" transaction
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during internal API calls
   */
  @PostMapping({"/validate", "/validate/"})
  public ResponseEntity<Void> validateAbortedTransaction(@RequestParam Map<String, String> transactionData)
    throws BadInputException, NotFoundException, PaymentServiceException {

    if (!transactionData.containsKey(WEBPAY_ABORTION_TOKEN_HEADER_NAME)) { // aborted
      throw new BadInputException("No transaction token was provided");
    }
    String token = transactionData.get(WEBPAY_ABORTION_TOKEN_HEADER_NAME);
    URI transactionUri = service.confirmTransaction(token, true);
    return ResponseEntity
        .status(SEE_OTHER)
        .location(transactionUri)
        .build();
  }

  @GetMapping({"/result/{token}", "/result/{token}/"})
  public SellPojo getTransactionResultFor(@NotBlank @PathVariable String token) throws NotFoundException {
    return service.getResultingTransaction(token);
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(PaymentServiceException.class)
  public String handleException(PaymentServiceException ex) {
    return ex.getMessage();
  }
}
