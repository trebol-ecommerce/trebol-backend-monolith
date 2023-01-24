package org.trebol.operation.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.trebol.pojo.AuthorizedAccessPojo;
import org.trebol.pojo.UserDetailsPojo;
import org.trebol.security.AuthorizationHeaderParserService;
import org.trebol.security.AuthorizedApiService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
  @InjectMocks AccessController instance;
  @Mock AuthorizationHeaderParserService<Claims> jwtClaimsParserServiceMock;
  @Mock UserDetailsService userDetailsServiceMock;
  @Mock AuthorizedApiService authorizedApiServiceMock;

  @Test
  void parses_authorization_header() {
    String fakeAuthorizationHeader = "Bearer TEST";
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn(fakeAuthorizationHeader);
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(new DefaultClaims(Map.of("sub", "username")));
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(null);

    AuthorizedAccessPojo result = instance.getApiRoutesAccess(new HttpHeaders());

    assertNull(result);
    verify(jwtClaimsParserServiceMock).parseToken("TEST");
    verify(userDetailsServiceMock).loadUserByUsername("username");
  }

  @Test
  void fetches_list_of_available_api_routes() {
    UserDetails userDetails = new UserDetailsPojo(
      List.of(),
      "username",
      "password",
      true,
      true,
      true,
      true);
    List<String> expectedRoutesList = List.of();
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn("Bearer TEST");
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(new DefaultClaims(Map.of("sub", "some")));
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(authorizedApiServiceMock.getAuthorizedApiRoutes(any(UserDetails.class))).thenReturn(expectedRoutesList);

    AuthorizedAccessPojo result = instance.getApiRoutesAccess(new HttpHeaders());

    assertNotNull(result);
    assertTrue(result.getRoutes().isEmpty());
    assertEquals(expectedRoutesList, result.getRoutes());
  }
}
