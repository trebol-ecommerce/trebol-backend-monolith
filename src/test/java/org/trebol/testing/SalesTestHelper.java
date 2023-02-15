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

package org.trebol.testing;

import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.jpa.entities.*;

import java.time.Instant;
import java.util.List;

import static org.trebol.config.Constants.BILLING_TYPE_INDIVIDUAL;
import static org.trebol.config.Constants.SELL_STATUS_PENDING;
import static org.trebol.testing.TestConstants.ANY;

/**
 * Builds & caches reusable instances of Sell and SellPojo
 */
public class SalesTestHelper {
  public static final long GENERIC_ID = 1L;
  public static final Instant GENERIC_DATE = Instant.now();
  public static final int SELL_DETAIL_UNITS = 1;
  public static final String SELL_PAYMENT_TYPE_NAME = "WebPay Plus";
  public static final int SELL_STATUS_CODE = 0;
  public static final int SELL_TOTAL_ITEMS = 1;
  public static final int SELL_NET_VALUE = 100;
  public static final int SELL_TRANSPORT_VALUE = 0;
  public static final int SELL_TAXES_VALUE = 0;
  public static final int SELL_TOTAL_VALUE = 100;
  public static final String SELL_TRANSACTION_TOKEN = "qwerty";
  private SellPojo pojoForFetch;
  private SellPojo pojoBeforeCreation;
  private SellPojo pojoAfterCreation;
  private Sell entityBeforeCreation;
  private Sell entityAfterCreation;
  private final PaymentType paymentType;
  private final BillingType billingType;
  final ProductsTestHelper productsHelper = new ProductsTestHelper();
  final CustomersTestHelper customersHelper = new CustomersTestHelper();

  public SalesTestHelper() {
     paymentType = PaymentType.builder()
       .id(GENERIC_ID)
       .name(SELL_PAYMENT_TYPE_NAME)
       .build();
     billingType = BillingType.builder()
       .id(GENERIC_ID)
       .name(BILLING_TYPE_INDIVIDUAL)
       .build();
  }

  public void resetSales() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public SellPojo sellPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = SellPojo.builder().buyOrder(GENERIC_ID).build();
    }
    return pojoForFetch;
  }

  public SellPojo sellPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      SellDetailPojo newDetailPojo = SellDetailPojo.builder()
        .units(SELL_DETAIL_UNITS)
        .product(productsHelper.productPojoBeforeCreationWithoutCategory())
        .build();
      pojoBeforeCreation = SellPojo.builder()
        .status(SELL_STATUS_PENDING)
        .details(List.of(newDetailPojo))
        .billingType(BILLING_TYPE_INDIVIDUAL)
        .paymentType(SELL_PAYMENT_TYPE_NAME)
        .customer(customersHelper.customerPojoBeforeCreation())
        .build();
    }
    return pojoBeforeCreation;
  }

  public SellPojo sellPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = SellPojo.builder()
        .buyOrder(GENERIC_ID)
        .token(SELL_TRANSACTION_TOKEN)
        .date(GENERIC_DATE)
        .status(SELL_STATUS_PENDING)
        .customer(customersHelper.customerPojoAfterCreation())
        .billingType(BILLING_TYPE_INDIVIDUAL)
        .paymentType(SELL_PAYMENT_TYPE_NAME)
        .details(List.of(
          SellDetailPojo.builder()
            .id(GENERIC_ID)
            .units(SELL_DETAIL_UNITS)
            .unitValue(productsHelper.productPojoAfterCreationWithoutCategory().getPrice())
            .product(productsHelper.productPojoAfterCreationWithoutCategory())
            .build()
        ))
        .netValue(SELL_NET_VALUE)
        .taxValue(SELL_TAXES_VALUE)
        .transportValue(SELL_TRANSPORT_VALUE)
        .totalValue(SELL_TOTAL_VALUE)
        .totalValue(SELL_TOTAL_ITEMS)
        .build();
    }
    return pojoAfterCreation;
  }

  public Sell sellEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = Sell.builder()
        .customer(customersHelper.customerEntityBeforeCreation())
        .status(SellStatus.builder()
          .id(1L)
          .code(1)
          .name("sellStatusName")
          .build())
        .paymentType(paymentType)
        .billingType(billingType)
        .billingAddress(Address.builder()
          .firstLine(ANY)
          .build())
        .details(List.of(
          SellDetail.builder()
            .product(productsHelper.productEntityBeforeCreationWithoutCategory())
            .units(SELL_DETAIL_UNITS)
            .unitValue(productsHelper.productEntityBeforeCreationWithoutCategory().getPrice())
            .build()
        ))
        .build();
    }
    return entityBeforeCreation;
  }

  public Sell sellEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = Sell.builder()
        .id(GENERIC_ID)
        .date(GENERIC_DATE)
        .transactionToken(SELL_TRANSACTION_TOKEN)
        .customer(customersHelper.customerEntityAfterCreation())
        .billingType(billingType)
        .paymentType(paymentType)
        .status(SellStatus.builder()
          .id(GENERIC_ID)
          .code(SELL_STATUS_CODE)
          .name("sellStatusName")
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
          SellDetail.builder()
            .id(GENERIC_ID)
            .units(1)
            .unitValue(productsHelper.productEntityAfterCreationWithoutCategory().getPrice())
            .product(productsHelper.productEntityAfterCreationWithoutCategory())
            .build()))
        .totalItems(1)
        .netValue(SELL_NET_VALUE)
        .transportValue(SELL_TRANSPORT_VALUE)
        .taxesValue(SELL_TAXES_VALUE)
        .totalValue(SELL_TOTAL_VALUE)
        .build();
    }
    return entityAfterCreation;
  }
}
