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
  private static ProductCategoryPojo pojoForFetch;
  private static ProductCategoryPojo pojoBeforeCreation;
  private static ProductCategoryPojo pojoAfterCreation;
  private static ProductCategory entityBeforeCreation;
  private static ProductCategory entityAfterCreation;

  public static void resetProductCategories() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static ProductCategoryPojo productCategoryPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new ProductCategoryPojo(CATEGORY_CODE);
    }
    return pojoForFetch;
  }

  public static ProductCategoryPojo productCategoryPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new ProductCategoryPojo(CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return pojoBeforeCreation;
  }

  public static ProductCategoryPojo productCategoryPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new ProductCategoryPojo(PRODUCT_ID, CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return pojoAfterCreation;
  }

  public static ProductCategory productCategoryEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new ProductCategory(CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return entityBeforeCreation;
  }

  public static ProductCategory productCategoryEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new ProductCategory(PRODUCT_ID, CATEGORY_CODE, CATEGORY_NAME, null);
    }
    return entityAfterCreation;
  }
}
