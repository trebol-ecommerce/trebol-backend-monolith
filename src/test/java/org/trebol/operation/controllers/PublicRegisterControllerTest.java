package org.trebol.operation.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.operation.services.RegistrationService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicRegisterControllerTest {
  @InjectMocks PublicRegisterController instance;
  @Mock RegistrationService registrationService;

  // TODO write a test
}
