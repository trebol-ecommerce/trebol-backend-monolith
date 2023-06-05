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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.services.conversion.ProductsConverterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductListItemsConverterServiceImplTest {
  @InjectMocks ProductListItemsConverterServiceImpl instance;
  @Mock ProductsConverterService productsConverterServiceMock;

  @Test
  void converts_listitem_to_product_pojo() {
    ProductListItem input = ProductListItem.builder()
      .product(Product.builder().build())
      .build();
    ProductPojo expectedResult = ProductPojo.builder().build();
    when(productsConverterServiceMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    ProductPojo result = instance.convertToPojo(input);
    assertEquals(expectedResult, result);
  }

  @Test
  void does_not_support_conveting_product_to_new_listitem_entity() {
    ProductPojo input = ProductPojo.builder().build();
    UnsupportedOperationException result = assertThrows(UnsupportedOperationException.class, () -> instance.convertToNewEntity(input));
    assertEquals("Not implemented", result.getMessage());
  }
}
