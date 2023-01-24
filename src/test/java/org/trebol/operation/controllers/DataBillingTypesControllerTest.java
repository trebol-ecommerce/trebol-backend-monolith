package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.BillingTypePojo;

@ExtendWith(MockitoExtension.class)
class DataBillingTypesControllerTest {
  @InjectMocks DataBillingTypesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<BillingType> sortService;
  @Mock GenericCrudService<BillingTypePojo, BillingType> crudService;
  @Mock IPredicateService<BillingType> predicateService;

  // TODO write a test
}
