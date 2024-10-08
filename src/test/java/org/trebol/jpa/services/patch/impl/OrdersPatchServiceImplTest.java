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

package org.trebol.jpa.services.patch.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.OrderPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.entities.OrderStatus;
import org.trebol.jpa.repositories.PaymentTypesRepository;
import org.trebol.jpa.repositories.OrderStatusesRepository;
import org.trebol.jpa.repositories.ShippersRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class OrdersPatchServiceImplTest {
    @InjectMocks
    OrdersPatchServiceImpl instance;
    @Mock
    OrderStatusesRepository statusesRepositoryMock;
    @Mock PaymentTypesRepository paymentTypesRepositoryMock;
    @Mock ShippersRepository shippersRepositoryMock;
    private static ObjectMapper MAPPER;
    private static Order existingOrder;

    @BeforeAll
    static void beforeAll() {
        MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .setSerializationInclusion(NON_NULL)
            .configure(WRITE_DATES_AS_TIMESTAMPS, false);
        existingOrder = Order.builder()
            .id(1L)
            .date(Instant.now())
            .totalItems(1)
            .netValue(9700)
            .transportValue(0)
            .taxesValue(300)
            .totalValue(10000)
            .transactionToken(ANY)
            .paymentType(PaymentType.builder()
                .id(1L)
                .name(ANY)
                .build())
            .status(OrderStatus.builder()
                .id(1L)
                .name(ANY)
                .build())
            .billingType(BillingType.builder()
                .id(1L)
                .name(ANY)
                .build())
            .billingAddress(null)
            .customer(null)
            .details(null)
            .billingCompany(null)
            .shipper(null)
            .shippingAddress(null)
            .salesperson(null)
            .build();
    }

    @Test
    void performs_empty_patch() throws BadInputException {
        Map<String, Object> input = this.mapFrom(OrderPojo.builder().build());
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertEquals(existingOrder, result);
    }

    @Test
    void patches_date() throws BadInputException {
        Instant whenTheMethodExecutes = Instant.now(Clock.systemUTC());
        Map<String, Object> input = this.mapFrom(OrderPojo.builder()
            .date(whenTheMethodExecutes)
            .build());
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertNotEquals(existingOrder, result);
        assertEquals(whenTheMethodExecutes, result.getDate());
    }

    @Test
    void patches_status() throws BadInputException {
        OrderStatus existingStatus = OrderStatus.builder().id(2L).build();
        Map<String, Object> input = this.mapFrom(OrderPojo.builder()
            .status(NOT_ANY)
            .build());
        when(statusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingStatus));
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertNotEquals(existingOrder, result);
        assertEquals(existingStatus, result.getStatus());
    }

    @Test
    void patches_paymentType() throws BadInputException {
        PaymentType existingPaymentType = PaymentType.builder().id(2L).build();
        Map<String, Object> input = this.mapFrom(OrderPojo.builder()
            .paymentType(NOT_ANY)
            .build());
        when(paymentTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingPaymentType));
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertNotEquals(existingOrder, result);
        assertEquals(existingPaymentType, result.getPaymentType());
    }

    @Test
    void patches_shipper() throws BadInputException {
        Shipper existingShipper = Shipper.builder().id(2L).build();
        Map<String, Object> input = this.mapFrom(OrderPojo.builder()
            .shipper(NOT_ANY)
            .build());
        when(shippersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingShipper));
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertNotEquals(existingOrder, result);
        assertEquals(existingShipper, result.getShipper());
    }

    @Test
    void patches_all_fields() throws BadInputException {
        Instant whenTheMethodExecutes = Instant.now();
        OrderStatus existingStatus = OrderStatus.builder().build();
        PaymentType existingPaymentType = PaymentType.builder().build();
        Shipper existingShipper = Shipper.builder().build();
        Map<String, Object> input = this.mapFrom(OrderPojo.builder()
            .date(whenTheMethodExecutes)
            .status(NOT_ANY)
            .paymentType(NOT_ANY)
            .shipper(NOT_ANY)
            .build());
        when(statusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingStatus));
        when(paymentTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingPaymentType));
        when(shippersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingShipper));
        Order result = instance.patchExistingEntity(input, existingOrder);
        assertNotEquals(existingOrder, result);
        assertEquals(whenTheMethodExecutes, result.getDate());
        assertEquals(existingStatus, result.getStatus());
        assertEquals(existingPaymentType, result.getPaymentType());
        assertEquals(existingShipper, result.getShipper());
    }

    @Test
    void does_not_support_old_method_signature() {
        OrderPojo input = OrderPojo.builder().build();
        assertThrows(UnsupportedOperationException.class,
            () -> instance.patchExistingEntity(input, existingOrder));
    }

    private Map<String, Object> mapFrom(OrderPojo data) {
        return MAPPER.convertValue(data, new TypeReference<HashMap<String, Object>>() {
        });
    }
}
