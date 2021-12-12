package org.trebol.operation;

import javassist.NotFoundException;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import java.net.URI;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface ICheckoutService {

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
   * @param wasAborted Whether the transaction was aborted by the user doing the payment.
   * @return The "completed/failed" URI for requesting it later on
   * @throws javassist.NotFoundException When no transaction matches the provided hash
   * @throws PaymentServiceException On unexpected failures
   */
  URI confirmTransaction(String token, boolean wasAborted) throws NotFoundException, PaymentServiceException;
}
