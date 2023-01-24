package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.CustomerPojo;

@ExtendWith(MockitoExtension.class)
class DataCustomersControllerTest {
  @InjectMocks DataCustomersController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Customer> sortService;
  @Mock CrudGenericService<CustomerPojo, Customer> crudService;
  @Mock PredicateService<Customer> predicateService;

  // TODO write a test
}
