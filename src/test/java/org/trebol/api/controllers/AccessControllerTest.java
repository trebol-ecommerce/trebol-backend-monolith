/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.controllers;

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
import org.trebol.api.models.AuthorizedAccessPojo;
import org.trebol.common.services.RegexMatcherAdapterService;
import org.trebol.security.UserDetailsPojo;
import org.trebol.security.services.AuthorizationHeaderParserService;
import org.trebol.security.services.AuthorizedApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.Claims.SUBJECT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.JWT_PREFIX;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class AccessControllerTest {
  final String FAKE_JWT = "TEST";
  @InjectMocks AccessController instance;
  @Mock AuthorizationHeaderParserService<Claims> jwtClaimsParserServiceMock;
  @Mock UserDetailsService userDetailsServiceMock;
  @Mock AuthorizedApiService authorizedApiServiceMock;
  @Mock RegexMatcherAdapterService regexMatcherAdapterService;
  UserDetails userDetails;

  @BeforeEach
  void beforeEach() {
    userDetails = UserDetailsPojo.builder()
      .authorities(List.of())
      .username("username")
      .password("password")
      .enabled(true)
      .accountNonLocked(true)
      .accountNonExpired(true)
      .credentialsNonExpired(true)
      .build();
  }

  @Test
  void parses_authorization_header() {
    DefaultClaims validClaims = new DefaultClaims(Map.of(
      SUBJECT, "username"));
    when(regexMatcherAdapterService.isAValidAuthorizationHeader(anyString())).thenReturn(true);
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn(JWT_PREFIX + FAKE_JWT);
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(validClaims);
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(null);

    ArrayList<Object> results = new ArrayList<>();
    results.add(instance.getApiRoutesAccess(new HttpHeaders()));
    results.add(instance.getApiResourceAccess(new HttpHeaders(), ANY));
    results.forEach(Assertions::assertNull);
    verify(jwtClaimsParserServiceMock, times(results.size())).parseToken(FAKE_JWT);
    verify(userDetailsServiceMock, times(results.size())).loadUserByUsername("username");
  }

  @Test
  void fetches_list_of_available_api_routes() {
    DefaultClaims validClaims = new DefaultClaims(Map.of(
      SUBJECT, "some"));
    List<String> expectedRoutesList = List.of();
    when(regexMatcherAdapterService.isAValidAuthorizationHeader(anyString())).thenReturn(true);
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn(JWT_PREFIX + FAKE_JWT);
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(validClaims);
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
    DefaultClaims validClaims = new DefaultClaims(Map.of(
      SUBJECT, "some"));
    List<String> expectedMethodsList = List.of();
    when(regexMatcherAdapterService.isAValidAuthorizationHeader(anyString())).thenReturn(true);
    when(jwtClaimsParserServiceMock.extractAuthorizationHeader(any(HttpHeaders.class))).thenReturn(JWT_PREFIX + FAKE_JWT);
    when(jwtClaimsParserServiceMock.parseToken(anyString())).thenReturn(validClaims);
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(userDetails);
    when(authorizedApiServiceMock.getAuthorizedApiRouteAccess(any(UserDetails.class), anyString())).thenReturn(expectedMethodsList);

    AuthorizedAccessPojo result = instance.getApiResourceAccess(new HttpHeaders(), ANY);

    assertNotNull(result);
    assertTrue(result.getPermissions().isEmpty());
    assertEquals(expectedMethodsList, result.getPermissions());
    verify(authorizedApiServiceMock).getAuthorizedApiRouteAccess(userDetails, ANY);
  }
}
