package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ShipperPojo;

@ExtendWith(MockitoExtension.class)
class DataShippersControllerTest {
  @InjectMocks DataShippersController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Shipper> sortService;
  @Mock GenericCrudService<ShipperPojo, Shipper> crudService;
  @Mock IPredicateService<Shipper> predicateService;

  // TODO write a test
}
