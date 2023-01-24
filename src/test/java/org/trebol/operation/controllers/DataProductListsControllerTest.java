package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductListPojo;

@ExtendWith(MockitoExtension.class)
class DataProductListsControllerTest {
  @InjectMocks DataProductListsController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<ProductList> sortService;
  @Mock GenericCrudService<ProductListPojo, ProductList> crudService;
  @Mock IPredicateService<ProductList> predicateService;

  // TODO write a test
}
