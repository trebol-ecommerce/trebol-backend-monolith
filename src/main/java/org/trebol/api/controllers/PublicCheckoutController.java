package org.trebol.api.controllers;

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
import org.trebol.api.ICheckoutService;

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
   * @return
   * @throws org.trebol.exceptions.BadInputException
   * @throws org.trebol.integration.exceptions.PaymentServiceException
   */
  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('checkout')")
  public PaymentRedirectionDetailsPojo submitCart(@Valid @RequestBody SellPojo transactionRequest)
    throws BadInputException, PaymentServiceException {
    SellPojo createdTransaction = service.saveCartAsPendingTransaction(transactionRequest);
    PaymentRedirectionDetailsPojo transactionRedirect = service.requestTransactionStart(createdTransaction);
    return transactionRedirect;
  }

  /**
   * Validate the status of a pending transaction and save the resulting metadata
   *
   * @param transactionData
   * @return
   * @throws org.trebol.exceptions.BadInputException
   * @throws javassist.NotFoundException
   * @throws org.trebol.integration.exceptions.PaymentServiceException
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
