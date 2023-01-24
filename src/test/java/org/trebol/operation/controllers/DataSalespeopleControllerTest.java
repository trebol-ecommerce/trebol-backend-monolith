package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SalespersonPojo;

@ExtendWith(MockitoExtension.class)
class DataSalespeopleControllerTest {
  @InjectMocks DataSalespeopleController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Salesperson> sortService;
  @Mock GenericCrudService<SalespersonPojo, Salesperson> crudService;
  @Mock IPredicateService<Salesperson> predicateService;

  // TODO write a test
}
