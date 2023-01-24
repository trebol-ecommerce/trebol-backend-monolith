package org.trebol.operation.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
  @InjectMocks AccessController instance;
  @Mock AuthorizationHeaderParserService<Claims> jwtClaimsParserServiceMock;
  @Mock UserDetailsService userDetailsServiceMock;
  @Mock AuthorizedApiService authorizedApiServiceMock;
  UserDetails userDetails;

  @BeforeEach
  void beforeEach() {
    userDetails = new UserDetailsPojo(
      List.of(),
      "username",
      "password",
      true,
      true,
      true,
      true);
  }

  @Test
  void parses_authorization_header() {
    String fakeAuthorizationHeader = "Bearer TEST";
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn(fakeAuthorizationHeader);
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(new DefaultClaims(Map.of("sub", "username")));
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(null);

    ArrayList<Object> results = new ArrayList<>();
    results.add(instance.getApiRoutesAccess(new HttpHeaders()));
    results.add(instance.getApiResourceAccess(new HttpHeaders(), ANY));
    results.forEach(Assertions::assertNull);
    verify(jwtClaimsParserServiceMock, times(results.size())).parseToken("TEST");
    verify(userDetailsServiceMock, times(results.size())).loadUserByUsername("username");
  }

  @Test
  void fetches_list_of_available_api_routes() {
    List<String> expectedRoutesList = List.of();
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn("Bearer TEST");
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(new DefaultClaims(Map.of("sub", "some")));
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(authorizedApiServiceMock.getAuthorizedApiRoutes(any(UserDetails.class))).thenReturn(expectedRoutesList);

    AuthorizedAccessPojo result = instance.getApiRoutesAccess(new HttpHeaders());

    assertNotNull(result);
    assertTrue(result.getRoutes().isEmpty());
    assertEquals(expectedRoutesList, result.getRoutes());
    verify(authorizedApiServiceMock).getAuthorizedApiRoutes(userDetails);
  }

  @Test
  void fetches_list_of_permitted_methods() {
    List<String> expectedMethodsList = List.of();
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn("Bearer TEST");
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(new DefaultClaims(Map.of("sub", "some")));
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(authorizedApiServiceMock.getAuthorizedApiRouteAccess(any(UserDetails.class), anyString())).thenReturn(expectedMethodsList);

    AuthorizedAccessPojo result = instance.getApiResourceAccess(new HttpHeaders(), ANY);

    assertNotNull(result);
    assertTrue(result.getPermissions().isEmpty());
    assertEquals(expectedMethodsList, result.getPermissions());
    verify(authorizedApiServiceMock).getAuthorizedApiRouteAccess(userDetails, ANY);
  }
}
