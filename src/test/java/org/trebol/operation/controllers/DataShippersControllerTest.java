package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ShipperPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataShippersControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<Shipper> sortService;
  @Mock GenericCrudJpaService<ShipperPojo, Shipper> crudService;
  @Mock IPredicateJpaService<Shipper> predicateService;
  @InjectMocks
  private DataShippersController instance;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
