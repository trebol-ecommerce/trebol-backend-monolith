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

@ExtendWith(MockitoExtension.class)
class ProductListCrudServiceImplTest {
  @InjectMocks ProductListsCrudServiceImpl instance;
  @Mock ProductListsRepository productListRepositoryMock;
  @Mock ProductListItemsRepository productListItemRepositoryMock;

  @Test
  void delete_whenProductListNotFound_throwsEntityNotFoundException() {
    when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(0L);
    assertThrows(EntityNotFoundException.class, () -> instance.delete(new BooleanBuilder()));
  }

  @Test
  void delete_whenProductListFound_shouldCallDeleteOnRepositories() {
    ProductList productListMock = new ProductList();
    productListMock.setId(1L);
    List<ProductList> productListsMock = List.of(productListMock);
    when(productListRepositoryMock.count(any(Predicate.class))).thenReturn(1L);
    when(productListRepositoryMock.findAll(any(Predicate.class))).thenReturn(productListsMock);
    instance.delete(new BooleanBuilder());
    verify(productListItemRepositoryMock, times(productListsMock.size())).deleteByListId(1L);
    verify(productListRepositoryMock).deleteAll(productListsMock);
  }

  @Test
  void getExisting_whenIdNull_shouldReturnEmptyOptional() {
    ProductListPojo productListPojoMock = ProductListPojo.builder()
      .id(null)
      .name("productListPojoName")
      .build();
    Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
    assertTrue(actualProductListOptional.isEmpty());
  }

  @Test
  void getExisting_whenNameNull_shouldReturnEmptyOptional() {
    ProductListPojo productListPojoMock = ProductListPojo.builder().build();
    Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
    assertTrue(actualProductListOptional.isEmpty());
  }

  @Test
  void getExisting_whenNameNotNull_shouldReturnProductList() {
    ProductListPojo productListPojoMock = ProductListPojo.builder()
      .name("productListPojoName")
      .build();
    ProductList productListMock = new ProductList();
    productListMock.setName("productListName");
    when(productListRepositoryMock.findByName(anyString())).thenReturn(Optional.of(productListMock));
    Optional<ProductList> actualProductListOptional = instance.getExisting(productListPojoMock);
    verify(productListRepositoryMock).findByName(productListPojoMock.getName());
    assertTrue(actualProductListOptional.isPresent());
    assertEquals(productListMock, actualProductListOptional.get());
  }
}
