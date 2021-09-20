package org.trebol.integration;

import org.trebol.pojo.SellPojo;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.integration.exceptions.PaymentServiceException;

/**
 * Interface for requesting and validating payments through an external integration
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IPaymentsIntegrationService {
  PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException;
  int requestPaymentResult(String transactionToken) throws PaymentServiceException;
  String getPaymentResultPageUrl();
}
