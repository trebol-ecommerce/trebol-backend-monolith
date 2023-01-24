package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.PredicateService;
import org.trebol.operation.CheckoutService;
import org.trebol.pojo.SellPojo;

@ExtendWith(MockitoExtension.class)
class PublicCheckoutControllerTest {
  @InjectMocks PublicCheckoutController instance;
  @Mock CheckoutService service;
  @Mock CrudGenericService<SellPojo, Sell> salesCrudService;
  @Mock PredicateService<Sell> salesPredicateService;
  @Mock IMailingIntegrationService mailingIntegrationService;

  // TODO write a test
}
