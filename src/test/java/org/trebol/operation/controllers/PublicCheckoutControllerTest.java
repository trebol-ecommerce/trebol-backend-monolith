package org.trebol.operation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.operation.ICheckoutService;
import org.trebol.pojo.SellPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicCheckoutControllerTest {

  @Mock ICheckoutService service;
  @Mock GenericCrudJpaService<SellPojo, Sell> salesCrudService;
  @Mock IPredicateJpaService<Sell> salesPredicateService;
  @Mock IMailingIntegrationService mailingIntegrationService;
  private PublicCheckoutController instance;

  @BeforeEach
  void setUp() {
    instance = new PublicCheckoutController(
            service,
            salesCrudService,
            salesPredicateService,
            mailingIntegrationService
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

}
