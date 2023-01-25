package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.ProductListItemsJpaRepository;
import org.trebol.jpa.repositories.ProductListsJpaRepository;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.conversion.ProductListItemsConverterService;
import org.trebol.jpa.services.crud.ProductsCrudService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataProductListContentsControllerTest {
  @InjectMocks DataProductListContentsController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SortSpecService<ProductListItem> sortServiceMock;
  @Mock ProductListItemsJpaRepository listItemsRepositoryMock;
  @Mock ProductListsJpaRepository listsRepositoryMock;
  @Mock PredicateService<ProductListItem> listItemsPredicateServiceMock;
  @Mock ProductsCrudService productsCrudServiceMock;
  @Mock ProductListItemsConverterService listItemConverterServiceMock;
  ProductList listExample;
  ProductListItem listItemExample;
  Product productExample;

  @BeforeEach
  void beforeEach() {
    productExample = new Product();
    productExample.setBarcode(ANY);
    productExample.setName(ANY);
    listExample = new ProductList();
    listExample.setId(1L);
    listExample.setName(ANY);
    listExample.setCode(ANY);
    listItemExample = new ProductListItem(listExample, productExample);
  }

  @Test
  void reads_contents_of_list() throws BadInputException {
    ProductPojo expectedProduct = ProductPojo.builder().build();
    when(paginationServiceMock.determineRequestedPageSize(anyMap())).thenReturn(1);
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(listItemsRepositoryMock.findAll(nullable(Predicate.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(listItemExample)));
    when(listItemsRepositoryMock.count(nullable(Predicate.class))).thenReturn(1L);
    when(listItemConverterServiceMock.convertToPojo(any(ProductListItem.class))).thenReturn(expectedProduct);

    DataPagePojo<ProductPojo> result = instance.readContents(Map.of("listCode", ANY));

    assertNotNull(result);
    assertEquals(1, result.getTotalCount());
    assertFalse(result.getItems().isEmpty());
    assertEquals(1, result.getItems().size());
    assertEquals(expectedProduct, result.getItems().iterator().next());
  }

  @Test
  void adds_item_to_list_contents() throws BadInputException {
    ProductPojo newProduct = ProductPojo.builder()
      .barcode(ANY)
      .name(ANY)
      .build();
    ProductListItem newListItemEntity = new ProductListItem();
    newListItemEntity.setProduct(productExample);
    newListItemEntity.setList(listExample);
    when(listsRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(listExample));
    when(productsCrudServiceMock.getExisting(any(ProductPojo.class))).thenReturn(Optional.of(productExample));

    instance.addToContents(newProduct, Map.of("listCode", ANY));

    verify(listItemsRepositoryMock).save(newListItemEntity);
  }

  // TODO add test for updating list contents
}
