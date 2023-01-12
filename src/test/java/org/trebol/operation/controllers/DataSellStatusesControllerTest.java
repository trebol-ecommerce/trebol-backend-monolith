package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SellStatusPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSellStatusesControllerTest {
  @InjectMocks DataSellStatusesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<SellStatus> sortService;
  @Mock GenericCrudJpaService<SellStatusPojo, SellStatus> crudService;
  @Mock IPredicateJpaService<SellStatus> predicateService;

  // TODO write a test
}
