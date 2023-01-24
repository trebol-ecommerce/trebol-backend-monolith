package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.ShipperPojo;

@ExtendWith(MockitoExtension.class)
class DataShippersControllerTest {
  @InjectMocks DataShippersController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Shipper> sortService;
  @Mock CrudGenericService<ShipperPojo, Shipper> crudService;
  @Mock PredicateService<Shipper> predicateService;

  // TODO write a test
}
