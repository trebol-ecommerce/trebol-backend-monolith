package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.operation.IRegistrationService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicRegisterControllerTest {

  @Mock IRegistrationService registrationService;

  @Test
  void sanity_check() {
    PublicRegisterController service = instantiate();
    assertNotNull(service);
  }

  private PublicRegisterController instantiate() {
    return new PublicRegisterController(registrationService);
  }

}
