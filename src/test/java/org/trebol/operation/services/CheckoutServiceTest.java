package org.trebol.operation.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.integration.IPaymentsIntegrationService;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.ISalesProcessService;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_STARTED;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ONE;
import static org.trebol.testhelpers.SalesTestHelper.SELL_TRANSACTION_TOKEN;
import static org.trebol.testhelpers.SalesTestHelper.resetSales;
import static org.trebol.testhelpers.SalesTestHelper.sellPojoAfterCreation;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

  @Mock GenericCrudJpaService<SellPojo, Sell> salesCrudService;
  @Mock ISalesProcessService salesProcessService;
  @Mock IPredicateJpaService<Sell> salesPredicateService;
  @Mock IPaymentsIntegrationService paymentIntegrationService;

  @InjectMocks
  private CheckoutServiceImpl service;
  private static final String PAYMENT_URL = "https://example.com/pay";
  private static final Predicate MATCHER_PREDICATE = new BooleanBuilder();

  @Test
  void requests_transaction_start()
      throws BadInputException, PaymentServiceException, EntityNotFoundException {
    PaymentRedirectionDetailsPojo payload = new PaymentRedirectionDetailsPojo(PAYMENT_URL, SELL_TRANSACTION_TOKEN);
    resetSales();
    when(paymentIntegrationService.requestNewPaymentPageDetails(sellPojoAfterCreation())).thenReturn(payload);

    PaymentRedirectionDetailsPojo result = service.requestTransactionStart(sellPojoAfterCreation());

    verify(paymentIntegrationService).requestNewPaymentPageDetails(sellPojoAfterCreation());
    verify(salesProcessService).markAsStarted(sellPojoAfterCreation());
    assertEquals(result.getUrl(), PAYMENT_URL);
    assertEquals(result.getToken(), SELL_TRANSACTION_TOKEN);
  }

  @Test
  void acknowledges_successful_transaction()
      throws PaymentServiceException, EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
        "statusCode", SELL_STATUS_PAYMENT_STARTED,
        "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(paymentIntegrationService.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(0);
    when(salesProcessService.markAsPaid(sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, false);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(paymentIntegrationService).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService).markAsPaid(sellPojoAfterCreation());
    assertNull(result);
  }

  @DisplayName("Confirm transaction when status code is not equal to zero call mark as failed")
  @Test
  void acknowledges_successful_transaction_payment_integration_returns_1()
    throws PaymentServiceException, EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(paymentIntegrationService.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(1);
    when(salesProcessService.markAsFailed(sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, false);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(paymentIntegrationService).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService).markAsFailed(sellPojoAfterCreation());
    assertNull(result);
  }

  @DisplayName("Confirm transaction when status code is not equal to zero call mark as failed throws BadInputException")
  @Test
  void acknowledges_successful_transaction_payment_integration_returns_1_mark_as_failed_throws_bad_input_exception()
    throws PaymentServiceException, EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(paymentIntegrationService.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(1);
    when(salesProcessService.markAsFailed(sellPojoAfterCreation())).thenThrow(BadInputException.class);

    assertThrows(IllegalStateException.class, () -> service.confirmTransaction(SELL_TRANSACTION_TOKEN, false));

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(paymentIntegrationService, times(ONE)).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService, times(ONE)).markAsFailed(sellPojoAfterCreation());
  }

  @Test
  void acknowledges_aborted_transaction()
      throws PaymentServiceException, EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
        "statusCode", SELL_STATUS_PAYMENT_STARTED,
        "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(salesProcessService.markAsAborted(sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, true);

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(salesProcessService, times(ONE)).markAsAborted(sellPojoAfterCreation());
    assertNull(result);
  }

  @DisplayName("Confirm Transaction when mark as aborted catch BadInputException throw IllegalArgumentException")
  @Test
  void acknowledges_aborted_transaction_illegal_bad_input_exception() throws EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(sellPojoAfterCreation());
    when(salesProcessService.markAsAborted(sellPojoAfterCreation())).thenThrow(BadInputException.class);

    assertThrows(IllegalStateException.class,
      () -> service.confirmTransaction(SELL_TRANSACTION_TOKEN, true), "Transaction could not be confirmed");

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(salesProcessService, times(ONE)).markAsAborted(sellPojoAfterCreation());
  }

  @Test
  void throws_exceptions_at_unexisting_transactions_before_requesting_payments()
      throws PaymentServiceException, EntityNotFoundException, BadInputException {
    PaymentRedirectionDetailsPojo payload = new PaymentRedirectionDetailsPojo(PAYMENT_URL, SELL_TRANSACTION_TOKEN);
    String exceptionMessage = "No match";
    resetSales();
    when(paymentIntegrationService.requestNewPaymentPageDetails(sellPojoAfterCreation())).thenReturn(payload);
    doThrow(new EntityNotFoundException(exceptionMessage)).
        when(salesProcessService).markAsStarted(sellPojoAfterCreation());

    PaymentRedirectionDetailsPojo result = null;
    try {
      result = service.requestTransactionStart(sellPojoAfterCreation());
    } catch (Exception ex) {
      verify(paymentIntegrationService).requestNewPaymentPageDetails(sellPojoAfterCreation());
    }

    assertNull(result);
    verify(salesProcessService).markAsStarted(sellPojoAfterCreation());
  }

  @Test
  void throws_exceptions_at_invalid_transactions_before_confirming()
      throws PaymentServiceException, EntityNotFoundException {
    Map<String, String> matcherMap = Map.of(
        "statusCode", SELL_STATUS_PAYMENT_STARTED,
        "token", SELL_TRANSACTION_TOKEN);
    String exceptionMessage = "No match";
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenThrow(new EntityNotFoundException(exceptionMessage));

    SellPojo result = null;
    try {
      result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, true);
    } catch (EntityNotFoundException e) {
      assertEquals(e.getMessage(), exceptionMessage);
    }

    assertNull(result);
  }

  @DisplayName("Generate result page proper url to URI")
  @Test
  void generate_result_page_url() {

    when(paymentIntegrationService.getPaymentResultPageUrl()).thenReturn("http://www.any.com");

    URI actual = service.generateResultPageUrl(ANY);

    assertEquals("http://www.any.com?token=ANY", actual.toString());
    verify(paymentIntegrationService, times(ONE)).getPaymentResultPageUrl();
  }

  @DisplayName("Generate result page proper url to URI when result page generates malformed url then catch " +
    "MalformedURLException and throw IllegalArgumentException")
  @Test
  void generate_result_page_url_throws_illegal_argument_exception() {

    when(paymentIntegrationService.getPaymentResultPageUrl()).thenReturn(ANY);

    assertThrows(IllegalStateException.class, () -> service.generateResultPageUrl(ANY), "Transaction was confirmed, but server had an unexpected malfunction");

    verify(paymentIntegrationService, times(ONE)).getPaymentResultPageUrl();
  }
}
