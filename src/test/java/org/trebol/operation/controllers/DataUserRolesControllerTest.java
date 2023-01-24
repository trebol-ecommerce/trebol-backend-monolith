package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserRolePojo;

@ExtendWith(MockitoExtension.class)
class DataUserRolesControllerTest {
  @InjectMocks DataUserRolesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<UserRole> sortService;
  @Mock GenericCrudService<UserRolePojo, UserRole> crudService;
  @Mock IPredicateService<UserRole> predicateService;

  // TODO write a test
}
