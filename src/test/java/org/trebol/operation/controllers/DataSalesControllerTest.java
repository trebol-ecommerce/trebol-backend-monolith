package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.jpa.services.ISortSpecService;
import org.trebol.operation.ISalesProcessService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SellPojo;

@ExtendWith(MockitoExtension.class)
class DataSalesControllerTest {
  @InjectMocks DataSalesController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecService<Sell> sortService;
  @Mock GenericCrudService<SellPojo, Sell> crudService;
  @Mock IPredicateService<Sell> predicateService;
  @Mock ISalesProcessService salesProcessService;
  @Mock IMailingIntegrationService mailingIntegrationService;

  // TODO write a test
}
