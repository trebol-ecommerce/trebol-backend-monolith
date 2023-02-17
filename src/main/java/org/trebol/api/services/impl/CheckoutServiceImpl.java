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

package org.trebol.api.services.impl;

import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.api.services.CheckoutService;
import org.trebol.api.services.SalesProcessService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.jpa.services.predicates.SalesPredicateService;
import org.trebol.payment.PaymentService;
import org.trebol.payment.PaymentServiceException;

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_STARTED;

@Service
public class CheckoutServiceImpl
  implements CheckoutService {
  private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
  private final SalesCrudService salesCrudService;
  private final SalesProcessService salesProcessService;
  private final SalesPredicateService salesPredicateService;
  private final PaymentService paymentIntegrationService;

  @Autowired
  public CheckoutServiceImpl(
    SalesCrudService salesCrudService,
    SalesProcessService salesProcessService,
    SalesPredicateService salesPredicateService,
    PaymentService paymentIntegrationService
  ) {
    this.salesCrudService = salesCrudService;
    this.salesProcessService = salesProcessService;
    this.salesPredicateService = salesPredicateService;
    this.paymentIntegrationService = paymentIntegrationService;
  }

  @Override
  public PaymentRedirectionDetailsPojo requestTransactionStart(SellPojo transaction) throws PaymentServiceException {
    PaymentRedirectionDetailsPojo response = paymentIntegrationService.requestNewPaymentPageDetails(transaction);
    try {
      transaction.setToken(response.getToken());
      salesProcessService.markAsStarted(transaction);
      return response;
    } catch (EntityNotFoundException | BadInputException exc) {
      throw new IllegalStateException("The server had a problem requesting the transaction", exc);
    }
  }

  @Override
  public SellPojo confirmTransaction(String transactionToken, boolean wasAborted)
    throws EntityNotFoundException, PaymentServiceException {
    SellPojo sellByToken = this.getSellRequestedWithMatchingToken(transactionToken);
    try {
      if (wasAborted) {
        return salesProcessService.markAsAborted(sellByToken);
      } else {
        return this.processSellPaymentStatus(sellByToken);
      }
    } catch (BadInputException e) {
      logger.error("Incorrect state of sell, was: {}", sellByToken.getStatus());
      throw new IllegalStateException("Transaction could not be confirmed");
    }
  }

  @Override
  public URI generateResultPageUrl(String transactionToken) {
    // TODO add a switch to either use path params or query params
    try {
      String url = (paymentIntegrationService.getPaymentResultPageUrl() + "?token=" + transactionToken);
      return new URL(url).toURI();
    } catch (MalformedURLException | URISyntaxException ex) {
      logger.error("Malformed redirection URL; make sure the 'final URL for payment method' property is correctly configured.", ex);
      throw new IllegalStateException("Transaction was confirmed, but server had an unexpected malfunction");
    }
  }

  /**
   * Fetches the result of a transaction from the payment payment service and updates it in the database.
   *
   * @throws EntityNotFoundException If no transaction has a matching token.
   * @throws PaymentServiceException As raised at payment level.
   */
  private SellPojo processSellPaymentStatus(SellPojo sellByToken)
    throws EntityNotFoundException, PaymentServiceException {
    int statusCode = paymentIntegrationService.requestPaymentResult(sellByToken.getToken());
    try {
      if (statusCode != 0) {
        return salesProcessService.markAsFailed(sellByToken);
      } else {
        return salesProcessService.markAsPaid(sellByToken);
      }
    } catch (BadInputException e) {
      logger.error("Incorrect state of sell, was: {}", sellByToken.getStatus());
      throw new IllegalStateException("Transaction could not be confirmed");
    }
  }

  private SellPojo getSellRequestedWithMatchingToken(String transactionToken) throws EntityNotFoundException {
    Map<String, String> startedWithTokenMatcher = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", transactionToken);
    Predicate startedTransactionWithMatchingToken = salesPredicateService.parseMap(startedWithTokenMatcher);
    return salesCrudService.readOne(startedTransactionWithMatchingToken);
  }
}
