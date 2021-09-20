package org.trebol.integration.payments.webpayplus;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.pojo.SellPojo;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.integration.exceptions.PaymentServiceException;

import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.exception.TransactionCommitException;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.model.WebpayPlusTransactionCommitResponse;
import cl.transbank.webpay.webpayplus.model.WebpayPlusTransactionCreateResponse;


import cl.transbank.webpay.exception.TransactionCreateException;

import org.trebol.integration.IPaymentsIntegrationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class WebpayplusPaymentServiceImpl
  implements IPaymentsIntegrationService {

  private final Logger logger = LoggerFactory.getLogger(WebpayplusPaymentServiceImpl.class);
  private final WebpayplusPaymentProperties properties;

  @Autowired
  public WebpayplusPaymentServiceImpl(WebpayplusPaymentProperties properties) {
    this.properties = properties;
  }

  @Override
  public PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException {

    if (properties.isProduction()) {
      WebpayPlus.Transaction.setCommerceCode(properties.getCommerceCode());
      WebpayPlus.Transaction.setApiKey(properties.getApiKey());
      WebpayPlus.Transaction.setIntegrationType(IntegrationType.LIVE);
    }

    String buyOrder = transaction.getBuyOrder().toString();
    String sessionId = String.valueOf(transaction.hashCode());
    double amount = transaction.getNetValue();
    String returnUrl = properties.getCallbackUrl();

    try {
      WebpayPlusTransactionCreateResponse webpayResponse = WebpayPlus.Transaction.create(
        buyOrder, sessionId, amount, returnUrl
      );
      PaymentRedirectionDetailsPojo response = new PaymentRedirectionDetailsPojo();
      response.setUrl(webpayResponse.getUrl());
      response.setToken(webpayResponse.getToken());
      return response;
    } catch (TransactionCreateException | IOException exc) {
      logger.error("Exception raised while creating transaction: ", exc);
      throw new PaymentServiceException("Webpay could not create a new transaction", exc);
    }
  }

  @Override
  public int requestPaymentResult(String transactionToken) throws PaymentServiceException {
    try {
      return WebpayPlus.Transaction.commit(transactionToken).getResponseCode();
    } catch (TransactionCommitException exc) {
      return 1;
    } catch (IOException exc) {
      logger.error("Exception raised while requesting transaction result: ", exc);
      throw new PaymentServiceException("Webpay service failed when confirming the transaction", exc);
    }
  }

  @Override
  public String getPaymentResultPageUrl() {
    return properties.getBrowserRedirectionUrl();
  }

}
