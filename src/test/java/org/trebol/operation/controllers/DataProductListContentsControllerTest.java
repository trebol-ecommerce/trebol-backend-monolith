package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.ProductListItemsJpaRepository;
import org.trebol.jpa.repositories.ProductListsJpaRepository;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.conversion.ProductListItemsConverterService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.ProductPojo;

@ExtendWith(MockitoExtension.class)
class DataProductListContentsControllerTest {
  @InjectMocks DataProductListContentsController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<ProductListItem> sortService;
  @Mock ProductListItemsJpaRepository listItemsRepository;
  @Mock ProductListsJpaRepository listsRepository;
  @Mock PredicateService<ProductListItem> listItemsPredicateService;
  @Mock CrudGenericService<ProductPojo, Product> productCrudService;
  @Mock ProductListItemsConverterService itemConverterService;


  // TODO write a test
}
