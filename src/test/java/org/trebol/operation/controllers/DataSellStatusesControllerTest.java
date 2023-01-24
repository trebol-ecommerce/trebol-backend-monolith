package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SellStatusPojo;

@ExtendWith(MockitoExtension.class)
class DataSellStatusesControllerTest {
  @InjectMocks DataSellStatusesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<SellStatus> sortService;
  @Mock GenericCrudService<SellStatusPojo, SellStatus> crudService;
  @Mock IPredicateService<SellStatus> predicateService;

  // TODO write a test
}
