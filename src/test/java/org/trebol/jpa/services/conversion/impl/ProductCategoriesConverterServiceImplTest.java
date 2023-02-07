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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesConverterServiceImplTest {
  @InjectMocks ProductCategoriesConverterServiceImpl instance;
  ProductCategory productCategory;
  ProductCategoryPojo productCategoryPojo;

  @BeforeEach
  void beforeEach() {
    productCategory = new ProductCategory();
    productCategory.setId(ID_1L);
    productCategoryPojo = ProductCategoryPojo.builder()
      .id(ID_1L)
      .name(ANY)
      .code(ANY)
      .build();
  }

  @AfterEach
  void afterEach() {
    productCategory = null;
    productCategoryPojo = null;
  }

  @Test
  void testConvertToPojo() {
    ProductCategoryPojo actual = instance.convertToPojo(productCategory);
    assertEquals(productCategory.getName(), actual.getName());
    assertEquals(productCategory.getCode(), actual.getCode());
  }

  @Test
  void testConvertToNewEntity() {
    ProductCategory actual = instance.convertToNewEntity(productCategoryPojo);
    assertEquals(productCategoryPojo.getName(), actual.getName());
    assertEquals(productCategoryPojo.getCode(), actual.getCode());
  }
}
