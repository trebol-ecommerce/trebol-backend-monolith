package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserPojo;

@ExtendWith(MockitoExtension.class)
class DataUsersControllerTest {
  @InjectMocks DataUsersController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<User> sortService;
  @Mock CrudGenericService<UserPojo, User> crudService;
  @Mock PredicateService<User> predicateService;

  // TODO write a test
}
