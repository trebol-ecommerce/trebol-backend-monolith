/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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
import org.trebol.api.models.OrderPojo;
import org.trebol.api.services.OrdersProcessService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.crud.OrdersCrudService;
import org.trebol.jpa.services.predicates.OrdersPredicateService;
import org.trebol.payment.PaymentService;
import org.trebol.payment.PaymentServiceException;
import org.trebol.testing.OrdersTestHelper;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.ORDER_STATUS_PAYMENT_STARTED;
import static org.trebol.testing.OrdersTestHelper.ORDER_TRANSACTION_TOKEN;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ONE;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {
    @InjectMocks CheckoutServiceImpl service;
    @Mock
    OrdersCrudService ordersCrudServiceMock;
    @Mock
    OrdersProcessService ordersProcessServiceMock;
    @Mock
    OrdersPredicateService ordersPredicateServiceMock;
    @Mock PaymentService paymentServiceMock;
    final OrdersTestHelper salesHelper = new OrdersTestHelper();
    static final String PAYMENT_URL = "https://example.com/pay";
    static final Predicate MATCHER_PREDICATE = new BooleanBuilder();

    @BeforeEach
    void beforeEach() {
        salesHelper.resetOrders();
    }

    @Test
    void requests_transaction_start()
        throws BadInputException, PaymentServiceException, EntityNotFoundException {
        PaymentRedirectionDetailsPojo payload = PaymentRedirectionDetailsPojo.builder()
            .url(PAYMENT_URL)
            .token(ORDER_TRANSACTION_TOKEN)
            .build();
        when(paymentServiceMock.requestNewPaymentPageDetails(salesHelper.orderPojoAfterCreation())).thenReturn(payload);

        PaymentRedirectionDetailsPojo result = service.requestTransactionStart(salesHelper.orderPojoAfterCreation());

        verify(paymentServiceMock).requestNewPaymentPageDetails(salesHelper.orderPojoAfterCreation());
        verify(ordersProcessServiceMock).markAsStarted(salesHelper.orderPojoAfterCreation());
        assertEquals(PAYMENT_URL, result.getUrl());
        assertEquals(ORDER_TRANSACTION_TOKEN, result.getToken());
    }

    @Test
    void acknowledges_successful_transaction()
        throws PaymentServiceException, EntityNotFoundException, BadInputException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.orderPojoAfterCreation());
        when(paymentServiceMock.requestPaymentResult(ORDER_TRANSACTION_TOKEN)).thenReturn(0);
        when(ordersProcessServiceMock.markAsPaid(salesHelper.orderPojoAfterCreation())).thenReturn(null);

        OrderPojo result = service.confirmTransaction(ORDER_TRANSACTION_TOKEN, false);

        verify(ordersPredicateServiceMock).parseMap(matcherMap);
        verify(ordersCrudServiceMock).readOne(MATCHER_PREDICATE);
        verify(paymentServiceMock).requestPaymentResult(ORDER_TRANSACTION_TOKEN);
        verify(ordersProcessServiceMock).markAsPaid(salesHelper.orderPojoAfterCreation());
        assertNull(result);
    }

    @DisplayName("Confirm transaction when status code is not equal to zero call mark as failed")
    @Test
    void acknowledges_successful_transaction_payment_integration_returns_1()
        throws PaymentServiceException, EntityNotFoundException, BadInputException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.orderPojoAfterCreation());
        when(paymentServiceMock.requestPaymentResult(ORDER_TRANSACTION_TOKEN)).thenReturn(1);
        when(ordersProcessServiceMock.markAsFailed(salesHelper.orderPojoAfterCreation())).thenReturn(null);

        OrderPojo result = service.confirmTransaction(ORDER_TRANSACTION_TOKEN, false);

        verify(ordersPredicateServiceMock).parseMap(matcherMap);
        verify(ordersCrudServiceMock).readOne(MATCHER_PREDICATE);
        verify(paymentServiceMock).requestPaymentResult(ORDER_TRANSACTION_TOKEN);
        verify(ordersProcessServiceMock).markAsFailed(salesHelper.orderPojoAfterCreation());
        assertNull(result);
    }

    @DisplayName("Confirm transaction when status code is not equal to zero call mark as failed throws BadInputException")
    @Test
    void acknowledges_successful_transaction_payment_integration_returns_1_mark_as_failed_throws_bad_input_exception()
        throws PaymentServiceException, EntityNotFoundException, BadInputException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.orderPojoAfterCreation());
        when(paymentServiceMock.requestPaymentResult(ORDER_TRANSACTION_TOKEN)).thenReturn(1);
        when(ordersProcessServiceMock.markAsFailed(salesHelper.orderPojoAfterCreation())).thenThrow(BadInputException.class);

        assertThrows(IllegalStateException.class, () -> service.confirmTransaction(ORDER_TRANSACTION_TOKEN, false));

        verify(ordersPredicateServiceMock, times(ONE)).parseMap(matcherMap);
        verify(ordersCrudServiceMock, times(ONE)).readOne(MATCHER_PREDICATE);
        verify(paymentServiceMock, times(ONE)).requestPaymentResult(ORDER_TRANSACTION_TOKEN);
        verify(ordersProcessServiceMock, times(ONE)).markAsFailed(salesHelper.orderPojoAfterCreation());
    }

    @Test
    void acknowledges_aborted_transaction()
        throws PaymentServiceException, EntityNotFoundException, BadInputException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.orderPojoAfterCreation());
        when(ordersProcessServiceMock.markAsAborted(salesHelper.orderPojoAfterCreation())).thenReturn(null);

        OrderPojo result = service.confirmTransaction(ORDER_TRANSACTION_TOKEN, true);

        verify(ordersPredicateServiceMock, times(ONE)).parseMap(matcherMap);
        verify(ordersCrudServiceMock, times(ONE)).readOne(MATCHER_PREDICATE);
        verify(ordersProcessServiceMock, times(ONE)).markAsAborted(salesHelper.orderPojoAfterCreation());
        assertNull(result);
    }

    @DisplayName("Confirm Transaction when mark as aborted catch BadInputException throw IllegalArgumentException")
    @Test
    void acknowledges_aborted_transaction_illegal_bad_input_exception() throws EntityNotFoundException, BadInputException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenReturn(salesHelper.orderPojoAfterCreation());
        when(ordersProcessServiceMock.markAsAborted(salesHelper.orderPojoAfterCreation())).thenThrow(BadInputException.class);

        assertThrows(IllegalStateException.class,
            () -> service.confirmTransaction(ORDER_TRANSACTION_TOKEN, true), "Transaction could not be confirmed");

        verify(ordersPredicateServiceMock, times(ONE)).parseMap(matcherMap);
        verify(ordersCrudServiceMock, times(ONE)).readOne(MATCHER_PREDICATE);
        verify(ordersProcessServiceMock, times(ONE)).markAsAborted(salesHelper.orderPojoAfterCreation());
    }

    @Test
    void throws_exceptions_at_unexisting_transactions_before_requesting_payments()
        throws PaymentServiceException, EntityNotFoundException, BadInputException {
        PaymentRedirectionDetailsPojo payload = PaymentRedirectionDetailsPojo.builder()
            .url(PAYMENT_URL)
            .token(ORDER_TRANSACTION_TOKEN)
            .build();
        String exceptionMessage = "No match";
        when(paymentServiceMock.requestNewPaymentPageDetails(salesHelper.orderPojoAfterCreation())).thenReturn(payload);
        doThrow(new EntityNotFoundException(exceptionMessage)).
            when(ordersProcessServiceMock).markAsStarted(salesHelper.orderPojoAfterCreation());

        PaymentRedirectionDetailsPojo result = null;
        try {
            result = service.requestTransactionStart(salesHelper.orderPojoAfterCreation());
        } catch (Exception ex) {
            verify(paymentServiceMock).requestNewPaymentPageDetails(salesHelper.orderPojoAfterCreation());
        }

        assertNull(result);
        verify(ordersProcessServiceMock).markAsStarted(salesHelper.orderPojoAfterCreation());
    }

    @Test
    void throws_exceptions_at_invalid_transactions_before_confirming()
        throws PaymentServiceException, EntityNotFoundException {
        Map<String, String> matcherMap = Map.of(
            "statusCode", ORDER_STATUS_PAYMENT_STARTED,
            "token", ORDER_TRANSACTION_TOKEN);
        String exceptionMessage = "No match";
        when(ordersPredicateServiceMock.parseMap(matcherMap)).thenReturn(MATCHER_PREDICATE);
        when(ordersCrudServiceMock.readOne(MATCHER_PREDICATE)).thenThrow(new EntityNotFoundException(exceptionMessage));

        OrderPojo result = null;
        try {
            result = service.confirmTransaction(ORDER_TRANSACTION_TOKEN, true);
        } catch (EntityNotFoundException e) {
            assertEquals(exceptionMessage, e.getMessage());
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
