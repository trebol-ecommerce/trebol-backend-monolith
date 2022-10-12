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
  @Mock
  IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  @Mock
  UserDetailsService userDetailsService;
  @Mock
  IAuthorizedApiService authorizedApiService;
  @InjectMocks
  private AccessController instance;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
