package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.CustomerPojo;

@ExtendWith(MockitoExtension.class)
class DataCustomersControllerTest {
  @InjectMocks DataCustomersController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Customer> sortService;
  @Mock GenericCrudService<CustomerPojo, Customer> crudService;
  @Mock IPredicateService<Customer> predicateService;

  // TODO write a test
}
