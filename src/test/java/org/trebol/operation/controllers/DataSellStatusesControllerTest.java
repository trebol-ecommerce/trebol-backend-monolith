package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.SellStatusPojo;

@ExtendWith(MockitoExtension.class)
class DataSellStatusesControllerTest {
  @InjectMocks DataSellStatusesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<SellStatus> sortService;
  @Mock CrudGenericService<SellStatusPojo, SellStatus> crudService;
  @Mock PredicateService<SellStatus> predicateService;

  // TODO write a test
}
