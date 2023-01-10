package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataProductCategoriesControllerTest {
  @InjectMocks DataProductCategoriesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<ProductCategory> sortService;
  @Mock GenericCrudJpaService<ProductCategoryPojo, ProductCategory> crudService;
  @Mock IPredicateJpaService<ProductCategory> predicateService;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
