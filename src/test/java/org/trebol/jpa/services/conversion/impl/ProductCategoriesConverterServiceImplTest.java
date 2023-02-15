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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.testing.ProductCategoriesTestHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ID_1L;

class ProductCategoriesConverterServiceImplTest {
  ProductCategoriesConverterServiceImpl instance;
  final ProductCategoriesTestHelper productCategoriesTestHelper = new ProductCategoriesTestHelper();

  @BeforeEach
  void beforeEach() {
    instance = new ProductCategoriesConverterServiceImpl();
    productCategoriesTestHelper.resetProductCategories();
  }

  @Test
  void converts_to_pojo() {
    ProductCategory input = productCategoriesTestHelper.productCategoryEntityAfterCreation();
    ProductCategoryPojo result = instance.convertToPojo(input);
    assertNotNull(result);
    assertEquals(input.getId(), result.getId());
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getCode(), result.getCode());
  }

  @Test
  void converting_to_pojo_does_not_include_parent() {
    ProductCategory input = ProductCategory.builder()
      .id(ID_1L)
      .name(ANY)
      .code(ANY)
      .parent(ProductCategory.builder()
        .name("ANY2")
        .code("ANY2")
        .build())
      .build();
    ProductCategoryPojo result = instance.convertToPojo(input);
    assertNotNull(result);
    assertNull(result.getParent());
  }

  @Test
  void converts_to_new_entity() {
    ProductCategoryPojo input = productCategoriesTestHelper.productCategoryPojoBeforeCreation();
    ProductCategory result = instance.convertToNewEntity(input);
    assertNotNull(result);
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getCode(), result.getCode());
  }
}
