package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductListPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataProductListsControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<ProductList> sortService;
  @Mock GenericCrudJpaService<ProductListPojo, ProductList> crudService;
  @Mock IPredicateJpaService<ProductList> predicateService;

  @Test
  void sanity_check() {
    DataProductListsController service = instantiate();
    assertNotNull(service);
  }

  private DataProductListsController instantiate() {
    return new DataProductListsController(
            paginationService,
            sortService,
            crudService,
            predicateService
    );
  }

}
