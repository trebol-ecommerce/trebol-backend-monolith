package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.crud.ProductCategoriesCrudService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataProductCategoriesControllerTest {
  @InjectMocks DataProductCategoriesController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SortSpecService<ProductCategory> sortServiceMock;
  @Mock ProductCategoriesCrudService crudServiceMock;
  @Mock PredicateService<ProductCategory> predicateServiceMock;

  @Test
  void reads_categories() {
    DataPagePojo<ProductCategoryPojo> pagePojo = new DataPagePojo<>(0, 0);
    when(crudServiceMock.readMany(anyInt(), anyInt(), eq(null), eq(null))).thenReturn(pagePojo);
    DataPagePojo<ProductCategoryPojo> result = instance.readMany(Map.of());
    assertNotNull(result);
    assertEquals(0, result.getTotalCount());
    assertTrue(result.getItems().isEmpty());
  }
}
