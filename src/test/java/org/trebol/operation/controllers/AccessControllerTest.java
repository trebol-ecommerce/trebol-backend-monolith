package org.trebol.operation.controllers;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.trebol.security.AuthorizationHeaderParserService;
import org.trebol.security.AuthorizedApiService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
  @InjectMocks AccessController instance;
  @Mock AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  @Mock UserDetailsService userDetailsService;
  @Mock AuthorizedApiService authorizedApiService;

  // TODO write a test
}
