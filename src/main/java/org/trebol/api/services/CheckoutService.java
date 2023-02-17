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

package org.trebol.api.services;

import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.payment.PaymentServiceException;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

/**
 * Core component of the client-side commerce process.<br/>
 * Helps consumers of the REST API to pay for their products; validate those payments; and finally
 * redirect them to a result page.
 */
public interface CheckoutService {

  /**
   * Fetch details to redirect the requester to the payment page; mark transaction as "started";
   * save metadata required for later confirmation
   *
   * @param transaction The "acknowledged" transaction
   * @return Details used by the requester to navigate to the payment page
   * @throws PaymentServiceException On unexpected failures
   */
  PaymentRedirectionDetailsPojo requestTransactionStart(SellPojo transaction) throws PaymentServiceException;

  /**
   * From a given token, assert existence of a transaction marked as "started"; fetch result of said transaction;
   * update saved metadata of that transaction<br/>
   * Usually, after this the client and the salesmanager are notified by some contact means, such as e-mail
   *
   * @param token      Previously emitted by the payment service
   * @param wasAborted Whether the transaction was aborted by the user doing the payment.
   * @return The "completed/failed" URI for requesting it later on
   * @throws EntityNotFoundException When no transaction matches the provided hash
   * @throws PaymentServiceException On unexpected failures
   */
  SellPojo confirmTransaction(String token, boolean wasAborted) throws EntityNotFoundException, PaymentServiceException;

  /**
   * From a given token, generate a corresponding URL to redirect users to view their receipt
   *
   * @param transactionToken Previously emitted by the payment service
   * @return The "completed/failed" URI to redirect consumer to
   */
  URI generateResultPageUrl(String transactionToken);
}
