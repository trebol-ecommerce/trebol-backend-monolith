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

import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;

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
      this.pojoForFetch = ProductCategoryPojo.builder()
        .code(CATEGORY_CODE)
        .build();
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
      this.entityBeforeCreation = ProductCategory.builder()
        .code(CATEGORY_CODE)
        .name(CATEGORY_NAME)
        .build();
    }
    return this.entityBeforeCreation;
  }

  public ProductCategory productCategoryEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = ProductCategory.builder()
        .id(PRODUCT_ID)
        .code(CATEGORY_CODE)
        .name(CATEGORY_NAME)
        .parent(null)
        .build();
    }
    return this.entityAfterCreation;
  }
}
