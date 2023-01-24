package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.SalespersonPojo;

@ExtendWith(MockitoExtension.class)
class DataSalespeopleControllerTest {
  @InjectMocks DataSalespeopleController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Salesperson> sortService;
  @Mock CrudGenericService<SalespersonPojo, Salesperson> crudService;
  @Mock PredicateService<Salesperson> predicateService;

  // TODO write a test
}
