package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.ISalesProcessService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.SellPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataSalesControllerTest {

  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<Sell> sortService;
  @Mock GenericCrudJpaService<SellPojo, Sell> crudService;
  @Mock IPredicateJpaService<Sell> predicateService;
  @Mock ISalesProcessService salesProcessService;
  @Mock IMailingIntegrationService mailingIntegrationService;
  @InjectMocks
  private DataSalesController instance;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
