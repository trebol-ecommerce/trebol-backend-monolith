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

package org.trebol.jpa.services.crud.impl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.OrderPojo;
import org.trebol.config.ApiProperties;
import org.trebol.jpa.entities.Order;
import org.trebol.jpa.repositories.OrdersRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.OrdersConverterService;
import org.trebol.testing.OrdersTestHelper;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdersCrudServiceImplTest {
    @InjectMocks OrdersCrudServiceImpl instance;
    @Mock OrdersRepository ordersRepositoryMock;
    @Mock OrdersConverterService ordersConverterMock;
    @Mock AddressesConverterService addressesConverterServiceMock; // TODO verify usage of this mock when reading one
    @Mock ApiProperties apiProperties; // TODO verify usage of this mock when doing a partial update
    final OrdersTestHelper ordersHelper = new OrdersTestHelper();

    @BeforeEach
    void beforeEach() {
        ordersHelper.resetOrders();
    }

    @Test
    void finds_by_example_using_buy_order() {
        OrderPojo input = ordersHelper.orderPojoForFetch();
        Order expectedResult = ordersHelper.orderEntityAfterCreation();
        when(ordersRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedResult));

        Optional<Order> match = instance.getExisting(input);

        verify(ordersRepositoryMock).findById(input.getBuyOrder());
        assertTrue(match.isPresent());
        assertEquals(expectedResult, match.get());
    }

    @Test
    void finds_using_predicate() throws EntityNotFoundException {
        OrderPojo expectedResult = OrderPojo.builder()
            .buyOrder(1L)
            .details(List.of()) // TODO put some details here to provide coverage to their conversion as well
            .build();
        when(ordersRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(ordersHelper.orderEntityAfterCreation()));
        when(ordersConverterMock.convertToPojo(any(Order.class))).thenReturn(expectedResult);

        OrderPojo result = instance.readOne(new BooleanBuilder());

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void throws_exception_when_not_found_using_predicates() {
        BooleanBuilder anyPredicate = new BooleanBuilder();
        when(ordersRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> instance.readOne(anyPredicate));
    }
}
