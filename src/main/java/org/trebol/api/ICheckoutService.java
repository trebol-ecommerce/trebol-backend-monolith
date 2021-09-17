package org.trebol.api;

import java.net.URI;

import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.exceptions.PaymentServiceException;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface ICheckoutService {

  /**
   * Analyze provided transaction, generate data for a transaction, mark it as "acknowledged" and save it
   * @param transaction The "requested" transaction
   * @return The "acknowledged" transaction
   * @throws org.trebol.exceptions.BadInputException
   */
  SellPojo saveCartAsPendingTransaction(SellPojo transaction) throws BadInputException;

  /**
   * Fetch details to redirect the requester to the payment page.Also mark transaction as "started"
 and save metadata required for confirmation
   * @param transaction The "acknowledged" transaction
   * @return Details used by the requester to navigate to the payment page
   * @throws PaymentServiceException On unexpected failures
   */
  PaymentRedirectionDetailsPojo requestTransactionStart(SellPojo transaction) throws PaymentServiceException;

  /**
   * Confirm existence of a "started" transaction, then fetch its result to update saved metadata of that
   * transaction, and notify both to salespeople and the client by e-mail
   * @param token Previously emitted by the payment service
   * @param wasAborted
   * @return The "completed/failed" URI for requesting it later on
   * @throws javassist.NotFoundException When no transaction matches the provided hash
   * @throws PaymentServiceException On unexpected failures
   */
  URI confirmTransaction(String token, boolean wasAborted) throws NotFoundException, PaymentServiceException;

  /**
   * Obtain all saved metadata for a transaction previously confirmed
   * @param token Previously emitted by the payment service
   * @return The "completed/cancelled/failed" transaction
   * @throws javassist.NotFoundException When no transaction matches the provided hash
   */
  SellPojo getResultingTransaction(String token) throws NotFoundException;
}
