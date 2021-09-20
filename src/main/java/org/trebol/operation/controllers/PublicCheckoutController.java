package org.trebol.operation.controllers;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.operation.ICheckoutService;

import javassist.NotFoundException;

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
   * Validate which token HTTP header was sent from WebPay Plus, then update the status of the pending transaction
   * matching said token.
   *
   * @param transactionData The HTTP headers
   * @return A 303 SEE OTHER response
   * @throws org.trebol.exceptions.BadInputException If no token resides in the expected HTTP headers.
   * @throws javassist.NotFoundException If the token matches no stored, "pending" transaction
   * @throws org.trebol.integration.exceptions.PaymentServiceException If an error happens during the payment
   *                                                                   integration process
   */
  @PostMapping({"/validate", "/validate/"})
  public ResponseEntity<Void> validateTransaction(@RequestParam Map<String, String> transactionData)
    throws BadInputException, NotFoundException, PaymentServiceException {

    String token;
    boolean wasAborted = false;
    if (transactionData.containsKey("token_ws")) { // success
      token = transactionData.get("token_ws");
    } else if (transactionData.containsKey("TBK_TOKEN")) { // aborted
      token = transactionData.get("TBK_TOKEN");
      wasAborted = true;
    } else {
      throw new BadInputException("No transaction token was provided");
    }
    URI transactionUri = service.confirmTransaction(token, wasAborted);
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
