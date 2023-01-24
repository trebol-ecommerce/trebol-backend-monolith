package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.PersonPojo;

@ExtendWith(MockitoExtension.class)
class DataPeopleControllerTest {
  @InjectMocks DataPeopleController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Person> sortService;
  @Mock GenericCrudService<PersonPojo, Person> crudService;
  @Mock IPredicateService<Person> predicateService;

  // TODO write a test
}
