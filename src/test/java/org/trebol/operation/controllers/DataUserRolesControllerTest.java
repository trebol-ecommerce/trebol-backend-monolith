package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserRolePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataUserRolesControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<UserRole> sortService;
  @Mock GenericCrudJpaService<UserRolePojo, UserRole> crudService;
  @Mock IPredicateJpaService<UserRole> predicateService;

  @Test
  void sanity_check() {
    DataUserRolesController service = instantiate();
    assertNotNull(service);
  }

  private DataUserRolesController instantiate() {
    return new DataUserRolesController(
            paginationService,
            sortService,
            crudService,
            predicateService
    );
  }

}
