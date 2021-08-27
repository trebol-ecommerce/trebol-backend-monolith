package org.trebol.integration;

import org.trebol.api.pojo.SellPojo;
import org.trebol.api.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.integration.exceptions.PaymentServiceException;

/**
 * Interface for requesting and validating payments through an external integration
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IPaymentsIntegrationService {
  public PaymentRedirectionDetailsPojo requestNewPaymentPageDetails(SellPojo transaction) throws PaymentServiceException;
  public Integer requestPaymentResult(String transactionToken) throws PaymentServiceException;
  public String getPaymentResultPageUrl();
}
