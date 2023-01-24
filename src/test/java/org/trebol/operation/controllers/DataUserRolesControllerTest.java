package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserRolePojo;

@ExtendWith(MockitoExtension.class)
class DataUserRolesControllerTest {
  @InjectMocks DataUserRolesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<UserRole> sortService;
  @Mock CrudGenericService<UserRolePojo, UserRole> crudService;
  @Mock PredicateService<UserRole> predicateService;

  // TODO write a test
}
