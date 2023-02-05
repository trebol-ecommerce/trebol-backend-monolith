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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.api.services.SalesProcessService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.integration.services.PaymentService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.testing.SalesTestHelper;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.SELL_STATUS_PAYMENT_STARTED;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ONE;
import static org.trebol.testing.SalesTestHelper.SELL_TRANSACTION_TOKEN;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {
  @InjectMocks CheckoutServiceImpl service;
  @Mock SalesCrudService salesCrudService;
  @Mock SalesProcessService salesProcessService;
  @Mock PredicateService<Sell> salesPredicateService;
  @Mock PaymentService paymentServiceMock;
  SalesTestHelper salesHelper = new SalesTestHelper();
  static final String PAYMENT_URL = "https://example.com/pay";
  static final Predicate MATCHER_PREDICATE = new BooleanBuilder();

  @BeforeEach
  void beforeEach() {
    salesHelper.resetSales();
  }

  @Test
  void requests_transaction_start()
    throws BadInputException, PaymentServiceException, EntityNotFoundException {
    PaymentRedirectionDetailsPojo payload = PaymentRedirectionDetailsPojo.builder()
      .url(PAYMENT_URL)
      .token(SELL_TRANSACTION_TOKEN)
      .build();
    when(paymentServiceMock.requestNewPaymentPageDetails(salesHelper.sellPojoAfterCreation())).thenReturn(payload);

    PaymentRedirectionDetailsPojo result = service.requestTransactionStart(salesHelper.sellPojoAfterCreation());

    verify(paymentServiceMock).requestNewPaymentPageDetails(salesHelper.sellPojoAfterCreation());
    verify(salesProcessService).markAsStarted(salesHelper.sellPojoAfterCreation());
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
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.sellPojoAfterCreation());
    when(paymentServiceMock.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(0);
    when(salesProcessService.markAsPaid(salesHelper.sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, false);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(paymentServiceMock).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService).markAsPaid(salesHelper.sellPojoAfterCreation());
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
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.sellPojoAfterCreation());
    when(paymentServiceMock.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(1);
    when(salesProcessService.markAsFailed(salesHelper.sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, false);

    verify(salesPredicateService).parseMap(matcherMap);
    verify(salesCrudService).readOne(MATCHER_PREDICATE);
    verify(paymentServiceMock).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService).markAsFailed(salesHelper.sellPojoAfterCreation());
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
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.sellPojoAfterCreation());
    when(paymentServiceMock.requestPaymentResult(SELL_TRANSACTION_TOKEN)).thenReturn(1);
    when(salesProcessService.markAsFailed(salesHelper.sellPojoAfterCreation())).thenThrow(BadInputException.class);

    assertThrows(IllegalStateException.class, () -> service.confirmTransaction(SELL_TRANSACTION_TOKEN, false));

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(paymentServiceMock, times(ONE)).requestPaymentResult(SELL_TRANSACTION_TOKEN);
    verify(salesProcessService, times(ONE)).markAsFailed(salesHelper.sellPojoAfterCreation());
  }

  @Test
  void acknowledges_aborted_transaction()
    throws PaymentServiceException, EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.sellPojoAfterCreation());
    when(salesProcessService.markAsAborted(salesHelper.sellPojoAfterCreation())).thenReturn(null);

    SellPojo result = service.confirmTransaction(SELL_TRANSACTION_TOKEN, true);

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(salesProcessService, times(ONE)).markAsAborted(salesHelper.sellPojoAfterCreation());
    assertNull(result);
  }

  @DisplayName("Confirm Transaction when mark as aborted catch BadInputException throw IllegalArgumentException")
  @Test
  void acknowledges_aborted_transaction_illegal_bad_input_exception() throws EntityNotFoundException, BadInputException {
    Map<String, String> matcherMap = Map.of(
      "statusCode", SELL_STATUS_PAYMENT_STARTED,
      "token", SELL_TRANSACTION_TOKEN);
    when(salesPredicateService.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
    when(salesCrudService.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.sellPojoAfterCreation());
    when(salesProcessService.markAsAborted(salesHelper.sellPojoAfterCreation())).thenThrow(BadInputException.class);

    assertThrows(IllegalStateException.class,
      () -> service.confirmTransaction(SELL_TRANSACTION_TOKEN, true), "Transaction could not be confirmed");

    verify(salesPredicateService, times(ONE)).parseMap(matcherMap);
    verify(salesCrudService, times(ONE)).readOne(MATCHER_PREDICATE);
    verify(salesProcessService, times(ONE)).markAsAborted(salesHelper.sellPojoAfterCreation());
  }

  @Test
  void throws_exceptions_at_unexisting_transactions_before_requesting_payments()
    throws PaymentServiceException, EntityNotFoundException, BadInputException {
    PaymentRedirectionDetailsPojo payload = PaymentRedirectionDetailsPojo.builder()
      .url(PAYMENT_URL)
      .token(SELL_TRANSACTION_TOKEN)
      .build();
    String exceptionMessage = "No match";
    when(paymentServiceMock.requestNewPaymentPageDetails(salesHelper.sellPojoAfterCreation())).thenReturn(payload);
    doThrow(new EntityNotFoundException(exceptionMessage)).
      when(salesProcessService).markAsStarted(salesHelper.sellPojoAfterCreation());

    PaymentRedirectionDetailsPojo result = null;
    try {
      result = service.requestTransactionStart(salesHelper.sellPojoAfterCreation());
    } catch (Exception ex) {
      verify(paymentServiceMock).requestNewPaymentPageDetails(salesHelper.sellPojoAfterCreation());
    }

    assertNull(result);
    verify(salesProcessService).markAsStarted(salesHelper.sellPojoAfterCreation());
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
    when(paymentServiceMock.getPaymentResultPageUrl()).thenReturn("http://www.any.com");

    URI actual = service.generateResultPageUrl(ANY);

    assertEquals("http://www.any.com?token=ANY", actual.toString());
    verify(paymentServiceMock, times(ONE)).getPaymentResultPageUrl();
  }

  @DisplayName("Generate result page proper url to URI when result page generates malformed url then catch " +
    "MalformedURLException and throw IllegalArgumentException")
  @Test
  void generate_result_page_url_throws_illegal_argument_exception() {
    when(paymentServiceMock.getPaymentResultPageUrl()).thenReturn(ANY);

    assertThrows(IllegalStateException.class, () -> service.generateResultPageUrl(ANY), "Transaction was confirmed, but server had an unexpected malfunction");

    verify(paymentServiceMock, times(ONE)).getPaymentResultPageUrl();
  }
}
