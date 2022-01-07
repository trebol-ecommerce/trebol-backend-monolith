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
  public WebpayplusPaymentServiceImpl(WebpayplusPaymentProperties properties) {
    this.properties = properties;
  }

  @Override
  public PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException {


    String buyOrder = transaction.getBuyOrder().toString();
    String sessionId = String.valueOf(transaction.hashCode());
    double amount = transaction.getNetValue();
    String returnUrl = properties.getCallbackUrl();

    WebpayPlus.Transaction webpayTransaction = this.createWebpayTransaction();

    try {
      WebpayPlusTransactionCreateResponse webpayResponse = webpayTransaction.create(
        buyOrder, sessionId, amount, returnUrl
      );
      PaymentRedirectionDetailsPojo response = new PaymentRedirectionDetailsPojo();
      response.setUrl(webpayResponse.getUrl());
      response.setToken(webpayResponse.getToken());
      return response;
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
    IntegrationType integrationType = IntegrationType.MOCK;
    if (properties.isProduction()) {
      commerceCode = properties.getCommerceCode();
      apiKey = properties.getApiKey();
      integrationType = IntegrationType.LIVE;
    }

    WebpayOptions wpOptions = new WebpayOptions(commerceCode, apiKey, integrationType);

    return new WebpayPlus.Transaction(wpOptions);
  }

}
