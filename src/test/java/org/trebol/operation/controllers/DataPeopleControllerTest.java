package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.PersonPojo;

@ExtendWith(MockitoExtension.class)
class DataPeopleControllerTest {
  @InjectMocks DataPeopleController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Person> sortService;
  @Mock CrudGenericService<PersonPojo, Person> crudService;
  @Mock PredicateService<Person> predicateService;

  // TODO write a test
}
