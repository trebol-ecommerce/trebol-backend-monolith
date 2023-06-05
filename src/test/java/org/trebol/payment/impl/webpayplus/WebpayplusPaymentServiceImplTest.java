package org.trebol.payment.impl.webpayplus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.payment.PaymentServiceException;
import org.trebol.testing.SalesTestHelper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class WebpayplusPaymentServiceImplTest {
  @InjectMocks WebpayplusPaymentServiceImpl instance;
  @Mock WebpayplusPaymentProperties propertiesMock;
  final SalesTestHelper salesHelper = new SalesTestHelper();
  static final String RETURN_URL = "http://localhost";

  @BeforeEach
  void beforeEach() {
    salesHelper.resetSales();
  }

  @Nested
  class TestMode {
    @Test
    void fetches_payment_page_for_started_transactions() throws PaymentServiceException {
      SellPojo input = salesHelper.sellPojoAfterCreation();
      when(propertiesMock.getCallbackUrl()).thenReturn(RETURN_URL);
      PaymentRedirectionDetailsPojo result = instance.requestNewPaymentPageDetails(input);
      assertNotNull(result);
      assertFalse(result.getToken().isBlank());
      assertFalse(result.getUrl().isBlank());
    }

    @Test
    void fetches_payment_result() {
      assertDoesNotThrow(() -> instance.requestPaymentResult(ANY));
    }

    @Test
    void fetches_payment_result_page_url() {
      when(propertiesMock.getBrowserRedirectionUrl()).thenReturn(ANY);
      String result = instance.getPaymentResultPageUrl();
      assertNotNull(result);
      assertEquals(ANY, result);
    }
  }

  @Nested
  class ProductionModeWithoutConfig {

    @BeforeEach
    void beforeEach() {
      when(propertiesMock.isProduction()).thenReturn(true);
    }

    @Test
    void will_not_begin_transactions() {
      SellPojo input = salesHelper.sellPojoAfterCreation();
      when(propertiesMock.getCallbackUrl()).thenReturn(RETURN_URL);
      PaymentServiceException result = assertThrows(PaymentServiceException.class, () -> instance.requestNewPaymentPageDetails(input));
      assertEquals("Webpay could not create a new transaction", result.getMessage());
    }

    @Test
    void will_not_fetch_transaction_result() throws PaymentServiceException {
      int result = instance.requestPaymentResult(ANY);
      assertEquals(1, result);
    }
  }
}
