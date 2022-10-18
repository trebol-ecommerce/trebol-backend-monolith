package org.trebol.testhelpers;

import org.trebol.jpa.entities.*;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import java.time.Instant;
import java.util.List;

import static org.trebol.testhelpers.CustomersTestHelper.*;
import static org.trebol.testhelpers.ProductsTestHelper.*;

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
  private static SellPojo pojoForFetch;
  private static SellPojo pojoBeforeCreation;
  private static SellPojo pojoAfterCreation;
  private static Sell entityBeforeCreation;
  private static Sell entityAfterCreation;

  public static void resetSales() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static SellPojo sellPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new SellPojo(GENERIC_ID);
    }
    return pojoForFetch;
  }

  public static SellPojo sellPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      SellDetailPojo newDetailPojo = SellDetailPojo.builder()
        .units(SELL_DETAIL_UNITS)
        .product(productPojoBeforeCreation())
        .build();
      pojoBeforeCreation = new SellPojo(List.of(newDetailPojo), SELL_BILLING_TYPE_NAME_PERSON, SELL_PAYMENT_TYPE_NAME,
                                        customerPojoBeforeCreation());
    }
    return pojoBeforeCreation;
  }

  public static SellPojo sellPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      SellDetailPojo persistedDetailPojo = SellDetailPojo.builder()
        .id(GENERIC_ID)
        .units(SELL_DETAIL_UNITS)
        .unitValue(productPojoAfterCreation().getPrice())
        .product(productPojoAfterCreation())
        .build();
      pojoAfterCreation = new SellPojo(GENERIC_ID, SELL_TRANSACTION_TOKEN, GENERIC_DATE, List.of(persistedDetailPojo),
                                       SELL_NET_VALUE, SELL_TAXES_VALUE, SELL_TRANSPORT_VALUE, SELL_TOTAL_VALUE,
                                       SELL_TOTAL_ITEMS, SELL_STATUS_NAME, SELL_BILLING_TYPE_NAME_PERSON,
                                       SELL_PAYMENT_TYPE_NAME, customerPojoAfterCreation(), null, null,
                                       null, null, null);
    }
    return pojoAfterCreation;
  }

  public static Sell sellEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      PaymentType paymentTypeEntity = new PaymentType(GENERIC_ID, SELL_PAYMENT_TYPE_NAME);
      BillingType billingTypeEntity = new BillingType(GENERIC_ID, SELL_BILLING_TYPE_NAME_PERSON);
      SellDetail newDetailEntity = new SellDetail(SELL_DETAIL_UNITS, productEntityBeforeCreation());
      newDetailEntity.setUnitValue(productEntityBeforeCreation().getPrice());
      entityBeforeCreation = new Sell(customerEntityBeforeCreation(),
                                      paymentTypeEntity,
                                      billingTypeEntity,
                                      List.of(newDetailEntity));
    }
    return entityBeforeCreation;
  }

  public static Sell sellEntityAfterCreation() {
    if (entityAfterCreation == null) {
      PaymentType paymentTypeEntity = new PaymentType(GENERIC_ID, SELL_PAYMENT_TYPE_NAME);
      BillingType billingTypeEntity = new BillingType(GENERIC_ID, SELL_BILLING_TYPE_NAME_PERSON);
      SellDetail persistedDetailEntity = new SellDetail(GENERIC_ID, SELL_DETAIL_UNITS,
                                                        productEntityAfterCreation().getPrice(),
                                                        productEntityAfterCreation());
      SellStatus sellStatusEntity = new SellStatus(GENERIC_ID, SELL_STATUS_CODE, SELL_STATUS_NAME);
      entityAfterCreation = new Sell(GENERIC_ID, GENERIC_DATE, SELL_TOTAL_ITEMS, SELL_NET_VALUE, SELL_TRANSPORT_VALUE,
                                     SELL_TAXES_VALUE, SELL_TOTAL_VALUE, SELL_TRANSACTION_TOKEN,
                                     customerEntityAfterCreation(), paymentTypeEntity, sellStatusEntity,
                                     billingTypeEntity, null, null, null, null, null, List.of(persistedDetailEntity));
    }
    return entityAfterCreation;
  }
}
