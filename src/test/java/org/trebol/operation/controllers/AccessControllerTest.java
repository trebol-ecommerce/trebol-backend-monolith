package org.trebol.operation.controllers;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.trebol.security.IAuthorizationHeaderParserService;
import org.trebol.security.IAuthorizedApiService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
  @InjectMocks AccessController instance;
  @Mock IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  @Mock UserDetailsService userDetailsService;
  @Mock IAuthorizedApiService authorizedApiService;

  // TODO write a test
}
