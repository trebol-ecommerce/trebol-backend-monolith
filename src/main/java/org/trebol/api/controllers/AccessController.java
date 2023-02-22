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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.trebol.api.models.AuthorizedAccessPojo;
import org.trebol.common.services.RegexMatcherAdapterService;
import org.trebol.security.services.AuthorizationHeaderParserService;
import org.trebol.security.services.AuthorizedApiService;

import java.util.Collection;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.trebol.config.Constants.JWT_PREFIX;

@RestController
@RequestMapping("/access")
@PreAuthorize("isAuthenticated()")
public class AccessController {
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final UserDetailsService userDetailsService;
  private final AuthorizedApiService authorizedApiService;
  private final RegexMatcherAdapterService regexMatcherService;

  @Autowired
  public AccessController(
    AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
    UserDetailsService userDetailsService,
    AuthorizedApiService authorizedApiService,
    RegexMatcherAdapterService regexMatcherService
  ) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.userDetailsService = userDetailsService;
    this.authorizedApiService = authorizedApiService;
    this.regexMatcherService = regexMatcherService;
  }

  @GetMapping({"", "/"})
  public AuthorizedAccessPojo getApiRoutesAccess(@RequestHeader HttpHeaders requestHeaders)
    throws UsernameNotFoundException, IllegalStateException {
    UserDetails userDetails = this.getUserDetails(requestHeaders);
    if (userDetails == null) {
      return null;
    }
    Collection<String> routes = authorizedApiService.getAuthorizedApiRoutes(userDetails);
    return AuthorizedAccessPojo.builder()
      .routes(routes)
      .build();
  }

  @GetMapping({"/{apiRoute}", "/{apiRoute}/"})
  public AuthorizedAccessPojo getApiResourceAccess(
    @RequestHeader HttpHeaders requestHeaders,
    @PathVariable String apiRoute)
    throws IllegalStateException {
    UserDetails userDetails = this.getUserDetails(requestHeaders);
    if (userDetails == null) {
      return null;
    }
    Collection<String> permissions = authorizedApiService.getAuthorizedApiRouteAccess(userDetails, apiRoute);
    return AuthorizedAccessPojo.builder()
      .permissions(permissions)
      .build();
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler({UsernameNotFoundException.class, IllegalStateException.class})
  public void handleException(Exception ex) {
    /*
      bad credentials method. whatever provided data is in token, didn't match with existing records of users.
      the consumer sent an invalid token. don't return an explanation of this. the status code should suffice.
      */
  }

  private UserDetails getUserDetails(HttpHeaders requestHeaders)
    throws UsernameNotFoundException, IllegalStateException {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader == null || !regexMatcherService.isAValidAuthorizationHeader(authorizationHeader)) {
      return null;
    }
    String jwt = authorizationHeader.replace(JWT_PREFIX, "");
    Claims body = jwtClaimsParserService.parseToken(jwt);
    String username = body.getSubject();
    return userDetailsService.loadUserByUsername(username);
  }
}
