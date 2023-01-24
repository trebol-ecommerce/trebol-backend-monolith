package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserPojo;

@ExtendWith(MockitoExtension.class)
class DataUsersControllerTest {
  @InjectMocks DataUsersController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<User> sortService;
  @Mock GenericCrudService<UserPojo, User> crudService;
  @Mock IPredicateService<User> predicateService;

  // TODO write a test
}
