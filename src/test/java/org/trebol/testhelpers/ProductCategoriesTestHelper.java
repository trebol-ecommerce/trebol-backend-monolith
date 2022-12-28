package org.trebol.testhelpers;

import org.trebol.jpa.entities.ProductCategory;
import org.trebol.pojo.ProductCategoryPojo;

/**
 * Builds & caches reusable instances of ProductCategory and ProductCategoryPojo
 */
public class ProductCategoriesTestHelper {

  public static final long PRODUCT_ID = 1L;
  public static final String CATEGORY_NAME = "test product name";
  public static final String CATEGORY_CODE = "TESTPROD1";
  private ProductCategoryPojo pojoForFetch;
  private ProductCategoryPojo pojoBeforeCreation;
  private ProductCategoryPojo pojoAfterCreation;
  private ProductCategory entityBeforeCreation;
  private ProductCategory entityAfterCreation;

  public void resetProductCategories() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public ProductCategoryPojo productCategoryPojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = ProductCategoryPojo.builder().code(CATEGORY_CODE).build();
    }
    return this.pojoForFetch;
  }

  public ProductCategoryPojo productCategoryPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = ProductCategoryPojo.builder()
        .code(CATEGORY_CODE)
        .name(CATEGORY_NAME)
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public ProductCategoryPojo productCategoryPojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = ProductCategoryPojo.builder()
        .id(PRODUCT_ID)
        .code(CATEGORY_CODE)
        .name(CATEGORY_NAME)
        .build();
    }
    return this.pojoAfterCreation;
  }

  public ProductCategory productCategoryEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = new ProductCategory(CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return this.entityBeforeCreation;
  }

  public ProductCategory productCategoryEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new ProductCategory(PRODUCT_ID, CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return this.entityAfterCreation;
  }
}
