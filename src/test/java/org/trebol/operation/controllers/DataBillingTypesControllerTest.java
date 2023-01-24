package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.BillingTypePojo;

@ExtendWith(MockitoExtension.class)
class DataBillingTypesControllerTest {
  @InjectMocks DataBillingTypesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<BillingType> sortService;
  @Mock CrudGenericService<BillingTypePojo, BillingType> crudService;
  @Mock PredicateService<BillingType> predicateService;

  // TODO write a test
}
