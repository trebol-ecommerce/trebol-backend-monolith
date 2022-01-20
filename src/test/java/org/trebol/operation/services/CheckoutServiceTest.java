package org.trebol.operation.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IPaymentsIntegrationService;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISellStepperJpaService;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.trebol.testhelpers.SalesTestHelper.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

  @Mock GenericCrudJpaService<SellPojo, Sell> salesCrudService;
  @Mock ISellStepperJpaService sellStepperService;
  @Mock IPredicateJpaService<Sell> salesPredicateService;
  @Mock IPaymentsIntegrationService paymentIntegrationService;
  private static final String PAYMENT_URL = "https://example.com/pay";
  private static final String RECEIPT_BASE_URL = "https://example2.com/callback";
  private static final String RECEIPT_URL = RECEIPT_BASE_URL + "/" + SELL_TRANSACTION_TOKEN;
  private static final Predicate MATCHER_PREDICATE = new BooleanBuilder();

  @Test
  void sanity_check() {
    CheckoutServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void requests_transaction_start()
      throws PaymentServiceException, EntityNotFoundException {
    PaymentRedirectionDetailsPojo payload = new PaymentRedirectionDetailsPojo(PAYMENT_URL, SELL_TRANSACTION_TOKEN);
    resetSales();
    when(paymentIntegrationService.requestNewPaymentPageDetails(sellPojoAfterCreation())).thenReturn(payload);
    CheckoutServiceImpl service = instantiate();

    PaymentRedirectionDetailsPojo result = service.requestTransactionStart(sellPojoAfterCreation());

    verify(paymentIntegrationService).requestNewPaymentPageDetails(sellPojoAfterCreation());
    verify(sellStepperService).setSellStatusToPaymentStartedWithToken(sellPojoAfterCreation().getBuyOrder(),
                                                                      result.getToken());
    assertEquals(result.getUrl(), PAYMENT_URL);
    assertEquals(result.getToken(), SELL_TRANSACTION_TOKEN);
  }

  @Test
  void acknowledges_successful_transaction()
      throws PaymentServiceException, EntityNotFoundException {
    Map<String, String> matcherMap = Map.of(
        "statusName", "Payment Started",
        "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(paymentIntegrationService.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(0);
    when(paymentIntegrationService.getPaymentResultPageUrl()).thenReturn(RECEIPT_BASE_URL);
    CheckoutServiceImpl service = instantiate();

    URI result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, false);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(paymentIntegrationService).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(sellStepperService).setSellStatusToPaidUnconfirmed(sellPojoAfterCreation().getBuyOrder());
    verify(paymentIntegrationService).getPaymentResultPageUrl();
    assertEquals(result.toString(), RECEIPT_URL);
  }

  @Test
  void acknowledges_aborted_transaction()
      throws PaymentServiceException, EntityNotFoundException {
    Map<String, String> matcherMap = Map.of(
        "statusName", "Payment Started",
        "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(paymentIntegrationService.getPaymentResultPageUrl()).thenReturn(RECEIPT_BASE_URL);
    CheckoutServiceImpl service = instantiate();

    URI result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, true);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(sellStepperService).setSellStatusToPaymentAborted(sellPojoAfterCreation().getBuyOrder());
    verify(paymentIntegrationService).getPaymentResultPageUrl();
    assertEquals(result.toString(), RECEIPT_URL);
  }

  @Test
  void throws_exceptions_at_unexisting_transactions_before_requesting_payments()
      throws PaymentServiceException, EntityNotFoundException {
    PaymentRedirectionDetailsPojo payload = new PaymentRedirectionDetailsPojo(PAYMENT_URL, SELL_TRANSACTION_TOKEN);
    String exceptionMessage = "No match";
    resetSales();
    when(paymentIntegrationService.requestNewPaymentPageDetails(sellPojoAfterCreation())).thenReturn(payload);
    doThrow(new EntityNotFoundException(exceptionMessage)).
        when(sellStepperService).setSellStatusToPaymentStartedWithToken(sellPojoAfterCreation().getBuyOrder(),
                                                                        SELL_TRANSACTION_TOKEN);
    CheckoutServiceImpl service = instantiate();

    PaymentRedirectionDetailsPojo result = null;
    try {
      result = service.requestTransactionStart(sellPojoAfterCreation());
    } catch (Exception ex) {
      verify(paymentIntegrationService).requestNewPaymentPageDetails(sellPojoAfterCreation());
    }

    assertNull(result);
    verify(sellStepperService).setSellStatusToPaymentStartedWithToken(sellPojoAfterCreation().getBuyOrder(),
                                                                      SELL_TRANSACTION_TOKEN);
  }

  @Test
  void throws_exceptions_at_invalid_transactions_before_confirming()
      throws PaymentServiceException, EntityNotFoundException {
    Map<String, String> matcherMap = Map.of(
        "statusName", "Payment Started",
        "token", SELL_TRANSACTION_TOKEN);
    String exceptionMessage = "No match";
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenThrow(new EntityNotFoundException(exceptionMessage));
    CheckoutServiceImpl service = instantiate();

    URI result = null;
    try {
      result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, true);
    } catch (EntityNotFoundException e) {
      assertEquals(e.getMessage(), exceptionMessage);
    }

    assertNull(result);
  }

  private CheckoutServiceImpl instantiate() {
    return new CheckoutServiceImpl(salesCrudService,
                                   sellStepperService,
                                   salesPredicateService,
                                   paymentIntegrationService);
  }
}
