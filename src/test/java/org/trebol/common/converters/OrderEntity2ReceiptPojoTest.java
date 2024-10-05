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

package org.trebol.common.converters;

import org.junit.jupiter.api.Test;
import org.trebol.api.models.ReceiptPojo;
import org.trebol.jpa.entities.Order;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.trebol.testing.TestConstants.ANY;

class OrderEntity2ReceiptPojoTest {
    final OrderEntity2ReceiptPojo instance = new OrderEntity2ReceiptPojo();

    @Test
    void converts_order_entities_to_receipt_pojos() {
        List.of(
            orderWithId(),
            orderWithIdAndDate(),
            orderWithIdAndDateAndTotalItems(),
            orderWithIdAndDateAndTotalItemsAndMonetaryValues(),
            orderWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken()
        ).forEach(sell -> {
            ReceiptPojo result = instance.convert(sell);
            assertNotNull(result);
            assertEquals(sell.getId(), result.getBuyOrder());
            assertEquals(sell.getDate(), result.getDate());
            assertEquals(sell.getTotalItems(), result.getTotalItems());
            assertEquals(sell.getTransportValue(), result.getTransportValue());
            assertEquals(sell.getTaxesValue(), result.getTaxValue());
            assertEquals(sell.getTotalValue(), result.getTotalValue());
            assertEquals(sell.getTransactionToken(), result.getToken());
        });
    }

    @Test
    void cannot_convert_incomplete_order_entities_to_receipt_pojos() {
        List.of(
            new Order()
        ).forEach(sell -> {
            assertThrows(Exception.class, () -> instance.convert(sell));
        });
    }

    Order orderWithId() {
        Order order = new Order();
        order.setId(1000L);
        return order;
    }

    Order orderWithIdAndDate() {
        Order order = orderWithId();
        order.setDate(Instant.now());
        return order;
    }

    Order orderWithIdAndDateAndTotalItems() {
        Order order = orderWithIdAndDate();
        order.setTotalItems(4);
        return order;
    }

    Order orderWithIdAndDateAndTotalItemsAndMonetaryValues() {
        Order order = orderWithIdAndDateAndTotalItems();
        order.setTransportValue(0);
        order.setTaxesValue(150);
        order.setTotalValue(2500);
        return order;
    }

    Order orderWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken() {
        Order order = orderWithIdAndDateAndTotalItemsAndMonetaryValues();
        order.setTransactionToken(ANY);
        return order;
    }
}
