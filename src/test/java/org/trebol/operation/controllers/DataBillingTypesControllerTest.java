package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.BillingTypePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataBillingTypesControllerTest {
  @Mock
  PaginationService paginationService;
  @Mock
  ISortSpecJpaService<BillingType> sortService;
  @Mock
  GenericCrudJpaService<BillingTypePojo, BillingType> crudService;
  @Mock
  IPredicateJpaService<BillingType> predicateService;
  @InjectMocks
  private DataBillingTypesController instance;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
