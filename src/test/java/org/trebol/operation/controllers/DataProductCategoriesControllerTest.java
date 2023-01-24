package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductCategoryPojo;

@ExtendWith(MockitoExtension.class)
class DataProductCategoriesControllerTest {
  @InjectMocks DataProductCategoriesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<ProductCategory> sortService;
  @Mock CrudGenericService<ProductCategoryPojo, ProductCategory> crudService;
  @Mock PredicateService<ProductCategory> predicateService;

  // TODO write a test
}
