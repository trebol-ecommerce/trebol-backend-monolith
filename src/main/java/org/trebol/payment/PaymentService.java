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

package org.trebol.payment;

import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;

/**
 * Interface for requesting and validating payments through an external payment
 */
public interface PaymentService {
  /**
   * Request an external payment service to generate a transaction process for us.
   *
   * @param transaction The details for the transaction.
   * @return The information with which to proceed to a payment page.
   * @throws PaymentServiceException If the payment service is caught under unexpected circumstances.
   */
  PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException;

  /**
   * Request the external payment service to report the status of the transaction matching a given token.
   *
   * @param transactionToken The token to match the transaction with.
   * @return The number code that represents the status of that transaction
   * @throws PaymentServiceException If the payment service is caught under unexpected circumstances.
   */
  int requestPaymentResult(String transactionToken) throws PaymentServiceException;

  /**
   * The configured frontend success page URL.
   *
   * @return Said URL.
   */
  String getPaymentResultPageUrl();
}
