package org.trebol.operation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataProductListContentsControllerTest {


  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<ProductListItem> sortService;
  @Mock IProductListItemsJpaRepository listItemsRepository;
  @Mock IProductListsJpaRepository listsRepository;
  @Mock IPredicateJpaService<ProductListItem> listItemsPredicateService;
  @Mock GenericCrudJpaService<ProductPojo, Product> productCrudService;
  @Mock ITwoWayConverterJpaService<ProductPojo, ProductListItem> itemConverterService;
  private DataProductListContentsController instance;

  @BeforeEach
  void setUp() {
    instance = new DataProductListContentsController(
            paginationService,
            sortService,
            listItemsRepository,
            listsRepository,
            listItemsPredicateService,
            productCrudService,
            itemConverterService
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
