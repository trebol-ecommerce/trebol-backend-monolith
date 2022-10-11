package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SalespersonPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSalespeopleControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<Salesperson> sortService;
  @Mock GenericCrudJpaService<SalespersonPojo, Salesperson> crudService;
  @Mock IPredicateJpaService<Salesperson> predicateService;
  @InjectMocks
  private DataSalespeopleController instance;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
