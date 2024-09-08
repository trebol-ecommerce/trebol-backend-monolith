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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.models.OrderDetailPojo;
import org.trebol.api.models.OrderPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Order;
import org.trebol.jpa.entities.OrderDetail;
import org.trebol.jpa.entities.OrderStatus;
import org.trebol.jpa.repositories.OrdersRepository;
import org.trebol.jpa.repositories.OrderDetailsRepository;
import org.trebol.jpa.repositories.OrderStatusesRepository;
import org.trebol.jpa.services.conversion.ProductsConverterService;
import org.trebol.jpa.services.conversion.OrdersConverterService;
import org.trebol.jpa.services.crud.OrdersCrudService;
import org.trebol.testing.ProductsTestHelper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.ORDER_STATUS_COMPLETED;
import static org.trebol.config.Constants.ORDER_STATUS_PAID_CONFIRMED;
import static org.trebol.config.Constants.ORDER_STATUS_PAID_UNCONFIRMED;
import static org.trebol.config.Constants.ORDER_STATUS_PAYMENT_CANCELLED;
import static org.trebol.config.Constants.ORDER_STATUS_PAYMENT_FAILED;
import static org.trebol.config.Constants.ORDER_STATUS_PAYMENT_STARTED;
import static org.trebol.config.Constants.ORDER_STATUS_PENDING;
import static org.trebol.config.Constants.ORDER_STATUS_REJECTED;

@ExtendWith(MockitoExtension.class)
class OrdersProcessServiceImplTest {
    @InjectMocks
    OrdersProcessServiceImpl instance;
    @Mock
    OrdersCrudService crudServiceMock;
    @Mock
    OrdersRepository ordersRepositoryMock;
    @Mock
    OrderStatusesRepository orderStatusesRepositoryMock;
    @Mock
    OrderDetailsRepository orderDetailsRepositoryMock;
    @Mock
    OrdersConverterService sellConverterServiceMock;
    @Mock ProductsConverterService productConverterServiceMock;
    final ProductsTestHelper productsHelper = new ProductsTestHelper();

    @BeforeEach
    void beforeEach() {
        productsHelper.resetProducts();
    }

    @Nested
    class MarkAsStarted {

        @Test
        void markAsStarted_SellStatus_IsNotPending_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock));

            assertThrows(BadInputException.class, () -> instance.markAsStarted(orderPojoMock));
        }

        @Test
        void markAsStarted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PENDING);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsStarted(orderPojoMock));
        }

        @Test
        void markAsStarted_ShouldReturn_SellPojo_WithStatusStarted() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PENDING);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_PAYMENT_STARTED, instance.markAsStarted(orderPojoMock).getStatus());
        }
    }

    @Nested
    class MarkAsAborted {

        @Test
        void markAsAborted_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsAborted(orderPojoMock));
        }

        @Test
        void markAsAborted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsAborted(orderPojoMock));
        }

        @Test
        void markAsAborted_ShouldReturn_SellPojo_WithStatusCancelled() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_PAYMENT_CANCELLED, instance.markAsAborted(orderPojoMock).getStatus());
        }
    }

    @Nested
    class MarkAsFailed {

        @Test
        void markAsFailed_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsFailed(orderPojoMock));
        }

        @Test
        void markAsFailed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsFailed(orderPojoMock));
        }

        @Test
        void markAsFailed_ShouldReturn_SellPojo_WithStatusFailed() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_PAYMENT_FAILED, instance.markAsFailed(orderPojoMock).getStatus());
        }
    }

    @Nested
    class MarkAsPaid {

        @Test
        void markAsPaid__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsPaid(orderPojoMock));
        }

        @Test
        void markAsPaid_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsPaid(orderPojoMock));
        }

        @Test
        void markAsPaid_ShouldReturn_SellPojo_WithStatusUnconfirmed() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_PAID_UNCONFIRMED, instance.markAsPaid(orderPojoMock).getStatus());
        }

        @Test
        void markAsPaid_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAYMENT_STARTED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            OrderDetail orderDetailMock = new OrderDetail();
            orderDetailMock.setId(1L);
            orderDetailMock.setUnits(11);
            orderDetailMock.setUnitValue(111);
            List<OrderDetail> orderDetailsMock = List.of(orderDetailMock);

            ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException
            when(orderDetailsRepositoryMock.findBySellId(any())).thenReturn(orderDetailsMock);
            when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

            Collection<OrderDetailPojo> actualSellDetailsPojo = instance.markAsPaid(orderPojoMock).getDetails();
            OrderDetailPojo actualOrderDetailPojo = actualSellDetailsPojo.iterator().next();

            assertEquals(1, actualSellDetailsPojo.size());
            assertEquals(11, actualOrderDetailPojo.getUnits());
            assertEquals(111, actualOrderDetailPojo.getUnitValue());
            assertEquals(productPojoMock, actualOrderDetailPojo.getProduct());
        }
    }

    @Nested
    class MarkAsConfirmed {

        @Test
        void markAsConfirmed__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsConfirmed(orderPojoMock));
        }

        @Test
        void markAsConfirmed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsConfirmed(orderPojoMock));
        }

        @Test
        void markAsConfirmed_ShouldReturn_SellPojo_WithStatusConfirmed() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_PAID_CONFIRMED, instance.markAsConfirmed(orderPojoMock).getStatus());
        }

        @Test
        void markAsConfirmed_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            OrderDetail orderDetailMock = new OrderDetail();
            orderDetailMock.setId(1L);
            orderDetailMock.setUnits(11);
            orderDetailMock.setUnitValue(111);
            List<OrderDetail> orderDetailsMock = List.of(orderDetailMock);

            ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException
            when(orderDetailsRepositoryMock.findBySellId(any())).thenReturn(orderDetailsMock);
            when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

            Collection<OrderDetailPojo> actualSellDetailsPojo = instance.markAsConfirmed(orderPojoMock).getDetails();
            OrderDetailPojo actualOrderDetailPojo = actualSellDetailsPojo.iterator().next();

            assertEquals(1, actualSellDetailsPojo.size());
            assertEquals(11, actualOrderDetailPojo.getUnits());
            assertEquals(111, actualOrderDetailPojo.getUnitValue());
            assertEquals(productPojoMock, actualOrderDetailPojo.getProduct());
        }
    }

    @Nested
    class MarkAsRejected {

        @Test
        void markAsRejected__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsRejected(orderPojoMock));
        }

        @Test
        void markAsRejected_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsRejected(orderPojoMock));
        }

        @Test
        void markAsRejected_ShouldReturn_SellPojo_WithStatusRejected() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_REJECTED, instance.markAsRejected(orderPojoMock).getStatus());
        }

        @Test
        void markAsRejected_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_UNCONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            OrderDetail orderDetailMock = new OrderDetail();
            orderDetailMock.setId(1L);
            orderDetailMock.setUnits(11);
            orderDetailMock.setUnitValue(111);
            List<OrderDetail> orderDetailsMock = List.of(orderDetailMock);

            ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException
            when(orderDetailsRepositoryMock.findBySellId(any())).thenReturn(orderDetailsMock);
            when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

            Collection<OrderDetailPojo> actualSellDetailsPojo = instance.markAsRejected(orderPojoMock).getDetails();
            OrderDetailPojo actualOrderDetailPojo = actualSellDetailsPojo.iterator().next();

            assertEquals(1, actualSellDetailsPojo.size());
            assertEquals(11, actualOrderDetailPojo.getUnits());
            assertEquals(111, actualOrderDetailPojo.getUnitValue());
            assertEquals(productPojoMock, actualOrderDetailPojo.getProduct());
        }
    }

    @Nested
    class MarkAsCompleted {

        @Test
        void markAsCompleted__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName("status");

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException

            assertThrows(BadInputException.class, () -> instance.markAsCompleted(orderPojoMock));
        }

        @Test
        void markAsCompleted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_CONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> instance.markAsCompleted(orderPojoMock));
        }

        @Test
        void markAsCompleted_ShouldReturn_SellPojo_WithStatusCompleted() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_CONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException

            assertEquals(ORDER_STATUS_COMPLETED, instance.markAsCompleted(orderPojoMock).getStatus());
        }

        @Test
        void markAsCompleted_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
            // Setup mock objects
            OrderPojo orderPojoMock = OrderPojo.builder().build();

            OrderStatus orderStatusMock = new OrderStatus();
            orderStatusMock.setName(ORDER_STATUS_PAID_CONFIRMED);

            Order orderMock = new Order();
            orderMock.setStatus(orderStatusMock);

            OrderDetail orderDetailMock = new OrderDetail();
            orderDetailMock.setId(1L);
            orderDetailMock.setUnits(11);
            orderDetailMock.setUnitValue(111);
            List<OrderDetail> orderDetailsMock = List.of(orderDetailMock);

            ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

            // Stubbing
            when(crudServiceMock.getExisting(any(OrderPojo.class))).thenReturn(Optional.of(orderMock)); // fetchExistingOrThrowException
            when(orderStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(orderStatusMock));
            when(sellConverterServiceMock.convertToPojo(any())).thenReturn(orderPojoMock); // convertOrThrowException
            when(orderDetailsRepositoryMock.findBySellId(any())).thenReturn(orderDetailsMock);
            when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

            Collection<OrderDetailPojo> actualSellDetailsPojo = instance.markAsCompleted(orderPojoMock).getDetails();
            OrderDetailPojo actualOrderDetailPojo = actualSellDetailsPojo.iterator().next();

            assertEquals(1, actualSellDetailsPojo.size());
            assertEquals(11, actualOrderDetailPojo.getUnits());
            assertEquals(111, actualOrderDetailPojo.getUnitValue());
            assertEquals(productPojoMock, actualOrderDetailPojo.getProduct());
        }
    }
}
