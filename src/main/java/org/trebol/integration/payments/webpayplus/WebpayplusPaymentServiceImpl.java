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

package org.trebol.integration.payments.webpayplus;

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
import org.trebol.integration.IPaymentsIntegrationService;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import java.io.IOException;

@Service
public class WebpayplusPaymentServiceImpl
  implements IPaymentsIntegrationService {
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
