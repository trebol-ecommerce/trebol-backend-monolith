package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.operation.SalesProcessService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SellPojo;

@ExtendWith(MockitoExtension.class)
class DataSalesControllerTest {
  @InjectMocks DataSalesController instance;
  @Mock PaginationService paginationService;
  @Mock SortSpecService<Sell> sortService;
  @Mock CrudGenericService<SellPojo, Sell> crudService;
  @Mock PredicateService<Sell> predicateService;
  @Mock SalesProcessService salesProcessService;
  @Mock IMailingIntegrationService mailingIntegrationService;

  // TODO write a test
}
