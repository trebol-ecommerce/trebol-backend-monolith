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

package org.trebol.payment.impl.webpayplus;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.exception.TransactionCommitException;
import cl.transbank.webpay.exception.TransactionCreateException;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.payment.PaymentService;
import org.trebol.payment.PaymentServiceException;

import java.io.IOException;

/**
 * Implements WebPay Plus as a payment gateway, using its official SDK.<br/>
 * Usage of WebPay Plus requires an affiliation to Transbank.<br/>
 * You can <a href="https://publico.transbank.cl/">know more about Transbank here</a>.<br/>
 * The documentation for the SDK and general use of WebPay Plus APIs
 * <a href="https://transbankdevelopers.cl/referencia/webpay">can be found here</a>.<br/>
 * <br/>
 * <b>Please note it is all in Spanish!</b>
 */
@Service
public class WebpayplusPaymentServiceImpl
  implements PaymentService {
  private final Logger logger = LoggerFactory.getLogger(WebpayplusPaymentServiceImpl.class);
  private final WebpayplusPaymentProperties properties;

  @Autowired
  public WebpayplusPaymentServiceImpl(
    WebpayplusPaymentProperties properties
  ) {
    this.properties = properties;
  }

  @Override
  public PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException {
    String buyOrder = transaction.getBuyOrder().toString();
    String sessionId = String.valueOf(transaction.hashCode());
    double amount = transaction.getTotalValue();
    String returnUrl = properties.getCallbackUrl();

    WebpayPlus.Transaction webpayTransaction = this.createWebpayTransaction();

    try {
      WebpayPlusTransactionCreateResponse webpayResponse = webpayTransaction.create(
        buyOrder, sessionId, amount, returnUrl
      );
      return PaymentRedirectionDetailsPojo.builder()
        .url(webpayResponse.getUrl())
        .token(webpayResponse.getToken())
        .build();
    } catch (TransactionCreateException | IOException exc) {
      logger.error("Exception raised while creating transaction: ", exc);
      throw new PaymentServiceException("Webpay could not create a new transaction");
    }
  }

  @Override
  public int requestPaymentResult(String transactionToken) throws PaymentServiceException {
    WebpayPlus.Transaction webpayTransaction = this.createWebpayTransaction();
    try {
      return webpayTransaction.commit(transactionToken).getResponseCode();
    } catch (TransactionCommitException exc) {
      return 1;
    } catch (IOException exc) {
      logger.error("Exception raised while requesting transaction result: ", exc);
      throw new PaymentServiceException("Webpay failed to confirm the transaction");
    }
  }

  @Override
  public String getPaymentResultPageUrl() {
    return properties.getBrowserRedirectionUrl();
  }

  private WebpayPlus.Transaction createWebpayTransaction() {
    String commerceCode = IntegrationCommerceCodes.WEBPAY_PLUS;
    String apiKey = IntegrationApiKeys.WEBPAY;
    IntegrationType integrationType = IntegrationType.TEST;
    if (Boolean.TRUE.equals(properties.isProduction())) {
      commerceCode = properties.getCommerceCode();
      apiKey = properties.getApiKey();
      integrationType = IntegrationType.LIVE;
    }

    WebpayOptions wpOptions = new WebpayOptions(commerceCode, apiKey, integrationType);

    return new WebpayPlus.Transaction(wpOptions);
  }
}
