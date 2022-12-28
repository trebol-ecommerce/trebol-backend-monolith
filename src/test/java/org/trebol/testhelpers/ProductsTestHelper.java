package org.trebol.testhelpers;

import org.trebol.jpa.entities.Product;
import org.trebol.pojo.ProductPojo;

/**
 * Builds & caches reusable instances of Product and ProductPojo
 */
public class ProductsTestHelper {

  public static final long PRODUCT_ID = 1L;
  public static final String PRODUCT_NAME = "test product name";
  public static final String PRODUCT_BARCODE = "TESTPROD1";
  public static final String PRODUCT_DESCRIPTION = "test product description";
  public static final int PRODUCT_PRICE = 100;
  public static final int PRODUCT_STOCK = 10;
  public static final int PRODUCT_STOCK_CRITICAL = 1;
  private ProductPojo pojoForFetch;
  private ProductPojo pojoBeforeCreation;
  private ProductPojo pojoAfterCreation;
  private Product entityBeforeCreation;
  private Product entityAfterCreation;

  public void resetProducts() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public ProductPojo productPojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = ProductPojo.builder().barcode(PRODUCT_BARCODE).build();
    }
    return this.pojoForFetch;
  }

  public ProductPojo productPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = ProductPojo.builder()
        .name(PRODUCT_NAME)
        .barcode(PRODUCT_BARCODE)
        .description(PRODUCT_DESCRIPTION)
        .price(PRODUCT_PRICE)
        .currentStock(PRODUCT_STOCK)
        .criticalStock(PRODUCT_STOCK_CRITICAL)
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public ProductPojo productPojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = ProductPojo.builder()
        .id(PRODUCT_ID)
        .name(PRODUCT_NAME)
        .barcode(PRODUCT_BARCODE)
        .description(PRODUCT_DESCRIPTION)
        .price(PRODUCT_PRICE)
        .currentStock(PRODUCT_STOCK)
        .criticalStock(PRODUCT_STOCK_CRITICAL)
        .build();
    }
    return pojoAfterCreation;
  }

  public Product productEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = new Product(PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION, PRODUCT_PRICE,
                                         PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL);
    }
    return this.entityBeforeCreation;
  }

  public Product productEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION,
                                        PRODUCT_PRICE, PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL, null);
    }
    return this.entityAfterCreation;
  }
}
