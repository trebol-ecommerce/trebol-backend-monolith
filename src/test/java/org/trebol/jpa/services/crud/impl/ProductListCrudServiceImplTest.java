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

package org.trebol.jpa.services.crud.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductListPojo;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.ProductListItemsRepository;
import org.trebol.jpa.repositories.ProductListsRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListCrudServiceImplTest {
  @InjectMocks ProductListsCrudServiceImpl instance;
  @Mock ProductListsRepository productListRepositoryMock;
  @Mock ProductListItemsRepository productListItemRepositoryMock;

  @Test
  void matches_productlist_from_name() {
    ProductListPojo input = ProductListPojo.builder()
      .name(ANY)
      .build();
    ProductList expectedResult = ProductList.builder().build();
    when(productListRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));
    Optional<ProductList> result = instance.getExisting(input);
    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
    verify(productListRepositoryMock).findByName(input.getName());
  }

  @Test
  void matches_productlist_from_id() {
    ProductListPojo input = ProductListPojo.builder()
      .id(1L)
      .build();
    ProductList expectedResult = ProductList.builder().build();
    when(productListRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedResult));
    Optional<ProductList> result = instance.getExisting(input);
    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());;
    verify(productListRepositoryMock).findById(input.getId());
  }

  @Test
  void cannot_match_any_productlist_from_null_data() {
    ProductListPojo input = ProductListPojo.builder().build();
    Optional<ProductList> actualProductListOptional = instance.getExisting(input);
    assertTrue(actualProductListOptional.isEmpty());
  }

  @Test
  void deletes_lists() {
    List<ProductList> productListsMock = List.of(
      ProductList.builder()
        .id(1L)
        .build()
    );
    when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(1L);
    when(productListRepositoryMock.findAll(any(Predicate.class))).thenReturn(productListsMock);
    instance.delete(new BooleanBuilder());
    verify(productListItemRepositoryMock, times(productListsMock.size())).deleteByListId(1L);
    verify(productListRepositoryMock).deleteAll(productListsMock);
  }

  @Test
  void attempting_to_delete_nothing_throws_EntityNotFoundException() {
    BooleanBuilder input = new BooleanBuilder();
    when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(0L);
    assertThrows(EntityNotFoundException.class, () -> instance.delete(input));
  }
}
