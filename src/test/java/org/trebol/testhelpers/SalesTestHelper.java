package org.trebol.testhelpers;

import org.trebol.jpa.entities.*;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import java.time.Instant;
import java.util.List;

/**
 * Builds & caches reusable instances of Sell and SellPojo
 */
public class SalesTestHelper {
  public static final long GENERIC_ID = 1L;
  public static final Instant GENERIC_DATE = Instant.now();
  public static final int SELL_DETAIL_UNITS = 1;
  public static final String SELL_BILLING_TYPE_NAME_PERSON = "Bill";
  public static final String SELL_PAYMENT_TYPE_NAME = "WebPay Plus";
  public static final int SELL_STATUS_CODE = 0;
  public static final String SELL_STATUS_NAME = "Requested";
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
  ProductsTestHelper productsHelper = new ProductsTestHelper();
  CustomersTestHelper customersHelper = new CustomersTestHelper();

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
        .product(productsHelper.productPojoBeforeCreation())
        .build();
      pojoBeforeCreation = SellPojo.builder()
        .details(List.of(newDetailPojo))
        .billingType(SELL_BILLING_TYPE_NAME_PERSON)
        .paymentType(SELL_PAYMENT_TYPE_NAME)
        .customer(customersHelper.customerPojoBeforeCreation())
        .build();
    }
    return pojoBeforeCreation;
  }

  public SellPojo sellPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      SellDetailPojo persistedDetailPojo = SellDetailPojo.builder()
        .id(GENERIC_ID)
        .units(SELL_DETAIL_UNITS)
        .unitValue(productsHelper.productPojoAfterCreation().getPrice())
        .product(productsHelper.productPojoAfterCreation())
        .build();
      pojoAfterCreation = SellPojo.builder()
        .buyOrder(GENERIC_ID)
        .token(SELL_TRANSACTION_TOKEN)
        .date(GENERIC_DATE)
        .details(List.of(persistedDetailPojo))
        .netValue(SELL_NET_VALUE)
        .taxValue(SELL_TAXES_VALUE)
        .transportValue(SELL_TRANSPORT_VALUE)
        .totalValue(SELL_TOTAL_VALUE)
        .totalValue(SELL_TOTAL_ITEMS)
        .status(SELL_STATUS_NAME)
        .billingType(SELL_BILLING_TYPE_NAME_PERSON)
        .paymentType(SELL_PAYMENT_TYPE_NAME)
        .customer(customersHelper.customerPojoAfterCreation())
        .build();
    }
    return pojoAfterCreation;
  }

  public Sell sellEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      PaymentType paymentTypeEntity = new PaymentType(GENERIC_ID, SELL_PAYMENT_TYPE_NAME);
      BillingType billingTypeEntity = new BillingType(GENERIC_ID, SELL_BILLING_TYPE_NAME_PERSON);
      SellDetail newDetailEntity = new SellDetail(SELL_DETAIL_UNITS, productsHelper.productEntityBeforeCreation());
      newDetailEntity.setUnitValue(productsHelper.productEntityBeforeCreation().getPrice());
      entityBeforeCreation = new Sell(customersHelper.customerEntityBeforeCreation(),
        paymentTypeEntity,
        billingTypeEntity,
        List.of(newDetailEntity));
    }
    return entityBeforeCreation;
  }

  public Sell sellEntityAfterCreation() {
    if (entityAfterCreation == null) {
      PaymentType paymentTypeEntity = new PaymentType(GENERIC_ID, SELL_PAYMENT_TYPE_NAME);
      BillingType billingTypeEntity = new BillingType(GENERIC_ID, SELL_BILLING_TYPE_NAME_PERSON);
      SellDetail persistedDetailEntity = new SellDetail(GENERIC_ID, SELL_DETAIL_UNITS,
        productsHelper.productEntityAfterCreation().getPrice(),
        productsHelper.productEntityAfterCreation());
      SellStatus sellStatusEntity = new SellStatus(GENERIC_ID, SELL_STATUS_CODE, SELL_STATUS_NAME);
      entityAfterCreation = new Sell(GENERIC_ID, GENERIC_DATE, SELL_TOTAL_ITEMS, SELL_NET_VALUE, SELL_TRANSPORT_VALUE,
        SELL_TAXES_VALUE, SELL_TOTAL_VALUE, SELL_TRANSACTION_TOKEN,
        customersHelper.customerEntityAfterCreation(), paymentTypeEntity, sellStatusEntity,
        billingTypeEntity, null, null, null, null, null, List.of(persistedDetailEntity));
    }
    return entityAfterCreation;
  }
}
