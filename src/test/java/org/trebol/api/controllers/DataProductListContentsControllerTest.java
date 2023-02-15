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

package org.trebol.api.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.ProductListItemsRepository;
import org.trebol.jpa.repositories.ProductListsRepository;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.conversion.ProductListItemsConverterService;
import org.trebol.jpa.services.crud.ProductsCrudService;
import org.trebol.jpa.services.predicates.ProductListItemsPredicateService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataProductListContentsControllerTest {
  @InjectMocks DataProductListContentsController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SortSpecParserService sortServiceMock;
  @Mock ProductListItemsRepository listItemsRepositoryMock;
  @Mock ProductListsRepository listsRepositoryMock;
  @Mock ProductListItemsPredicateService listItemsPredicateServiceMock;
  @Mock ProductsCrudService productsCrudServiceMock;
  @Mock ProductListItemsConverterService listItemConverterServiceMock;
  ProductList listExample;
  ProductListItem listItemExample;
  Product productExample;
  Map<String, String> simpleQueryParamsMap;

  @BeforeEach
  void beforeEach() {
    productExample = Product.builder()
      .barcode(ANY)
      .name(ANY)
      .build();
    listExample = ProductList.builder()
      .id(1L)
      .name(ANY)
      .code(ANY)
      .build();
    listItemExample = ProductListItem.builder()
      .list(listExample)
      .product(productExample)
      .build();
    simpleQueryParamsMap = Map.of("listCode", ANY);
  }

  @Test
  void reads_contents_of_list() throws BadInputException {
    ProductPojo expectedProduct = ProductPojo.builder().build();
    when(paginationServiceMock.determineRequestedPageSize(anyMap())).thenReturn(1);
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(listItemsRepositoryMock.findAll(nullable(Predicate.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(listItemExample)));
    when(listItemsRepositoryMock.count(nullable(Predicate.class))).thenReturn(1L);
    when(listItemConverterServiceMock.convertToPojo(any(ProductListItem.class))).thenReturn(expectedProduct);

    DataPagePojo<ProductPojo> result = instance.readContents(simpleQueryParamsMap);

    assertNotNull(result);
    assertEquals(1, result.getTotalCount());
    assertFalse(result.getItems().isEmpty());
    assertEquals(1, result.getItems().size());
    assertEquals(expectedProduct, result.getItems().iterator().next());
  }

  @Test
  void accepts_sorting_order() throws BadInputException {
    Map<String, String> queryParams = Map.of("listCode", ANY, "sortBy", ANY);
    Sort sortingOrder = Sort.by(ANY);
    BooleanBuilder expectedPredicate = new BooleanBuilder();
    Pageable expectedPagination = PageRequest.of(0, 1, sortingOrder);
    ProductPojo expectedProduct = ProductPojo.builder().build();
    when(paginationServiceMock.determineRequestedPageSize(anyMap())).thenReturn(1);
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(sortServiceMock.parse(anyMap(), anyMap())).thenReturn(sortingOrder);
    when(listItemsPredicateServiceMock.parseMap(anyMap())).thenReturn(expectedPredicate);
    when(listItemsRepositoryMock.findAll(nullable(Predicate.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(listItemExample)));
    when(listItemsRepositoryMock.count(nullable(Predicate.class))).thenReturn(1L);
    when(listItemConverterServiceMock.convertToPojo(any(ProductListItem.class))).thenReturn(expectedProduct);

    DataPagePojo<ProductPojo> result = instance.readContents(queryParams);

    assertNotNull(result);
    verify(listItemsRepositoryMock).findAll(expectedPredicate, expectedPagination);
  }

  @Test
  void adds_item_to_list_contents() throws BadInputException {
    ProductPojo newProduct = ProductPojo.builder()
      .barcode(ANY)
      .name(ANY)
      .build();
    ProductListItem newListItemEntity = ProductListItem.builder()
      .product(productExample)
      .list(listExample)
      .build();
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(productsCrudServiceMock.getExisting(any(ProductPojo.class))).thenReturn(Optional.of(productExample));

    instance.addToContents(newProduct, simpleQueryParamsMap);

    verify(listItemsRepositoryMock).save(newListItemEntity);
  }

  @Test
  void updates_list_contents() throws BadInputException {
    ProductPojo newProduct = ProductPojo.builder()
      .barcode(ANY)
      .name(ANY)
      .build();
    List<ProductPojo> inputProductList = List.of(newProduct);
    ProductListItem newListItemEntity = ProductListItem.builder()
      .product(productExample)
      .list(listExample)
      .build();
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(productsCrudServiceMock.getExisting(any(ProductPojo.class))).thenReturn(Optional.of(productExample));

    instance.updateContents(inputProductList, simpleQueryParamsMap);

    verify(listItemsRepositoryMock).deleteByListId(listExample.getId());
    verify(listItemsRepositoryMock).save(newListItemEntity);
  }

  @Test
  void deletes_from_list_contents() throws BadInputException {
    List<ProductListItem> contents = List.of(listItemExample);
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(listItemsRepositoryMock.findAll(nullable(Predicate.class))).thenReturn(contents);

    instance.deleteFromContents(simpleQueryParamsMap);

    verify(listItemsRepositoryMock).deleteAll(contents);
  }

  @Test
  void fails_when_target_list_is_not_specified() {
    Map<String, String> mapWithoutATargetList = Map.of("listCode", "");
    List.of(
      assertThrows(BadInputException.class, () -> instance.readContents(mapWithoutATargetList)),
      assertThrows(BadInputException.class, () -> instance.addToContents(null, mapWithoutATargetList)),
      assertThrows(BadInputException.class, () -> instance.updateContents(null, mapWithoutATargetList)),
      assertThrows(BadInputException.class, () -> instance.deleteFromContents(mapWithoutATargetList))
    ).forEach(exception -> assertEquals("listCode query param is required", exception.getMessage()));
  }

  @Test
  void fails_when_target_list_does_not_exist_or_cannot_be_found() {
    when(listsRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());

    List.of(
      assertThrows(EntityNotFoundException.class, () -> instance.readContents(simpleQueryParamsMap)),
      assertThrows(EntityNotFoundException.class, () -> instance.addToContents(null, simpleQueryParamsMap)),
      assertThrows(EntityNotFoundException.class, () -> instance.updateContents(null, simpleQueryParamsMap)),
      assertThrows(EntityNotFoundException.class, () -> instance.deleteFromContents(simpleQueryParamsMap))
    ).forEach(exception -> assertEquals("Requested item(s) not found", exception.getMessage()));
  }
}
