package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudService;
import org.trebol.jpa.services.IPredicateService;
import org.trebol.operation.ICheckoutService;
import org.trebol.pojo.SellPojo;

@ExtendWith(MockitoExtension.class)
class PublicCheckoutControllerTest {
  @InjectMocks PublicCheckoutController instance;
  @Mock ICheckoutService service;
  @Mock GenericCrudService<SellPojo, Sell> salesCrudService;
  @Mock IPredicateService<Sell> salesPredicateService;
  @Mock IMailingIntegrationService mailingIntegrationService;

  // TODO write a test
}
