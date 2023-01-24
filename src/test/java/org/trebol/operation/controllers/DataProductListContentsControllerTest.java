package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.jpa.services.conversion.IProductListItemsConverterService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ProductPojo;

@ExtendWith(MockitoExtension.class)
class DataProductListContentsControllerTest {
  @InjectMocks DataProductListContentsController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<ProductListItem> sortService;
  @Mock IProductListItemsJpaRepository listItemsRepository;
  @Mock IProductListsJpaRepository listsRepository;
  @Mock IPredicateService<ProductListItem> listItemsPredicateService;
  @Mock GenericCrudService<ProductPojo, Product> productCrudService;
  @Mock IProductListItemsConverterService itemConverterService;


  // TODO write a test
}
