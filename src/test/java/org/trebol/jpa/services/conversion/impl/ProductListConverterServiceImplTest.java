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

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductListPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.ProductListItemsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListConverterServiceImplTest {
  @InjectMocks ProductListConverterServiceImpl instance;
  @Mock ProductListItemsRepository productListItemRepositoryMock;

  @Test
  void converts_to_pojo() {
    ProductList input = ProductList.builder()
      .id(1L)
      .name(ANY)
      .code(ANY)
      .build();
    Mockito.when(productListItemRepositoryMock.count(Mockito.any(Predicate.class))).thenReturn(1L);
    ProductListPojo result = instance.convertToPojo(input);
    assertNotNull(result);
    assertEquals(input.getId(), result.getId());
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getCode(), result.getCode());
    assertEquals(1L, result.getTotalCount());
  }

  @Test
  void converts_to_new_entity() throws BadInputException {
    ProductListPojo input = ProductListPojo.builder()
      .name(ANY)
      .code(ANY)
      .build();
    ProductList result = instance.convertToNewEntity(input);
    assertNotNull(result);
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getCode(), result.getCode());
  }
}
