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

package org.trebol.testing;

import org.trebol.api.models.OrderDetailPojo;
import org.trebol.api.models.OrderPojo;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.entities.Order;

import java.time.Instant;
import java.util.List;

import static org.trebol.config.Constants.BILLING_TYPE_INDIVIDUAL;
import static org.trebol.config.Constants.ORDER_STATUS_PENDING;
import static org.trebol.testing.TestConstants.ANY;

/**
 * Builds & caches reusable instances of Sell and SellPojo
 */
public class OrdersTestHelper {
    public static final long GENERIC_ID = 1L;
    public static final Instant GENERIC_DATE = Instant.now();
    public static final int ORDER_DETAIL_UNITS = 1;
    public static final String ORDER_PAYMENT_TYPE_NAME = "WebPay Plus";
    public static final int ORDER_STATUS_CODE = 0;
    public static final int ORDER_TOTAL_ITEMS = 1;
    public static final int ORDER_NET_VALUE = 100;
    public static final int ORDER_TRANSPORT_VALUE = 0;
    public static final int ORDER_TAXES_VALUE = 0;
    public static final int ORDER_TOTAL_VALUE = 100;
    public static final String ORDER_TRANSACTION_TOKEN = "qwerty";
    private OrderPojo pojoForFetch;
    private OrderPojo pojoBeforeCreation;
    private OrderPojo pojoAfterCreation;
    private Order entityBeforeCreation;
    private Order entityAfterCreation;
    private final PaymentType paymentType;
    private final BillingType billingType;
    final ProductsTestHelper productsHelper = new ProductsTestHelper();
    final CustomersTestHelper customersHelper = new CustomersTestHelper();

    public OrdersTestHelper() {
        paymentType = PaymentType.builder()
            .id(GENERIC_ID)
            .name(ORDER_PAYMENT_TYPE_NAME)
            .build();
        billingType = BillingType.builder()
            .id(GENERIC_ID)
            .name(BILLING_TYPE_INDIVIDUAL)
            .build();
    }

    public void resetOrders() {
        pojoForFetch = null;
        pojoBeforeCreation = null;
        pojoAfterCreation = null;
        entityBeforeCreation = null;
        entityAfterCreation = null;
    }

    public OrderPojo orderPojoForFetch() {
        if (pojoForFetch==null) {
            pojoForFetch = OrderPojo.builder().buyOrder(GENERIC_ID).build();
        }
        return pojoForFetch;
    }

    public OrderPojo orderPojoBeforeCreation() {
        if (pojoBeforeCreation==null) {
            OrderDetailPojo newDetailPojo = OrderDetailPojo.builder()
                .units(ORDER_DETAIL_UNITS)
                .product(productsHelper.productPojoBeforeCreationWithoutCategory())
                .build();
            pojoBeforeCreation = OrderPojo.builder()
                .status(ORDER_STATUS_PENDING)
                .details(List.of(newDetailPojo))
                .billingType(BILLING_TYPE_INDIVIDUAL)
                .paymentType(ORDER_PAYMENT_TYPE_NAME)
                .customer(customersHelper.customerPojoBeforeCreation())
                .build();
        }
        return pojoBeforeCreation;
    }

    public OrderPojo orderPojoAfterCreation() {
        if (pojoAfterCreation==null) {
            pojoAfterCreation = OrderPojo.builder()
                .buyOrder(GENERIC_ID)
                .token(ORDER_TRANSACTION_TOKEN)
                .date(GENERIC_DATE)
                .status(ORDER_STATUS_PENDING)
                .customer(customersHelper.customerPojoAfterCreation())
                .billingType(BILLING_TYPE_INDIVIDUAL)
                .paymentType(ORDER_PAYMENT_TYPE_NAME)
                .details(List.of(
                    OrderDetailPojo.builder()
                        .units(ORDER_DETAIL_UNITS)
                        .unitValue(productsHelper.productPojoAfterCreationWithoutCategory().getPrice())
                        .product(productsHelper.productPojoAfterCreationWithoutCategory())
                        .build()
                ))
                .netValue(ORDER_NET_VALUE)
                .taxValue(ORDER_TAXES_VALUE)
                .transportValue(ORDER_TRANSPORT_VALUE)
                .totalValue(ORDER_TOTAL_VALUE)
                .totalValue(ORDER_TOTAL_ITEMS)
                .build();
        }
        return pojoAfterCreation;
    }

    public Order orderEntityBeforeCreation() {
        if (entityBeforeCreation==null) {
            entityBeforeCreation = Order.builder()
                .customer(customersHelper.customerEntityBeforeCreation())
                .status(OrderStatus.builder()
                    .id(1L)
                    .code(1)
                    .name("orderStatusName")
                    .build())
                .paymentType(paymentType)
                .billingType(billingType)
                .billingAddress(Address.builder()
                    .firstLine(ANY)
                    .build())
                .details(List.of(
                    OrderDetail.builder()
                        .product(productsHelper.productEntityBeforeCreationWithoutCategory())
                        .units(ORDER_DETAIL_UNITS)
                        .unitValue(productsHelper.productEntityBeforeCreationWithoutCategory().getPrice())
                        .build()
                ))
                .build();
        }
        return entityBeforeCreation;
    }

    public Order orderEntityAfterCreation() {
        if (entityAfterCreation==null) {
            entityAfterCreation = Order.builder()
                .id(GENERIC_ID)
                .date(GENERIC_DATE)
                .transactionToken(ORDER_TRANSACTION_TOKEN)
                .customer(customersHelper.customerEntityAfterCreation())
                .billingType(billingType)
                .paymentType(paymentType)
                .status(OrderStatus.builder()
                    .id(GENERIC_ID)
                    .code(ORDER_STATUS_CODE)
                    .name("orderStatusName")
                    .build())
                .billingAddress(Address.builder()
                    .firstLine("billingAddressFirstLine")
                    .city("billingAddressCity")
                    .municipality("billingAddressMunicipality")
                    .build())
                .shippingAddress(null)
                .shipper(null)
                .salesperson(null)
                .details(List.of(
                    OrderDetail.builder()
                        .id(GENERIC_ID)
                        .units(1)
                        .unitValue(productsHelper.productEntityAfterCreationWithoutCategory().getPrice())
                        .product(productsHelper.productEntityAfterCreationWithoutCategory())
                        .build()))
                .totalItems(1)
                .netValue(ORDER_NET_VALUE)
                .transportValue(ORDER_TRANSPORT_VALUE)
                .taxesValue(ORDER_TAXES_VALUE)
                .totalValue(ORDER_TOTAL_VALUE)
                .build();
        }
        return entityAfterCreation;
    }
}
