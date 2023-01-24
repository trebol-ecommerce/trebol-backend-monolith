package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductListPojo;

@ExtendWith(MockitoExtension.class)
class DataProductListsControllerTest {
  @InjectMocks DataProductListsController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<ProductList> sortService;
  @Mock CrudGenericService<ProductListPojo, ProductList> crudService;
  @Mock PredicateService<ProductList> predicateService;

  // TODO write a test
}
