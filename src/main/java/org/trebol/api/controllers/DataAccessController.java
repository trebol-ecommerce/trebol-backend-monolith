package org.trebol.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.AuthorizedAccessPojo;
import org.trebol.services.security.AuthorizationHeaderParserService;
import org.trebol.services.security.AuthorizedApiService;

@RestController
@RequestMapping("/access")
public class DataAccessController {

  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final UserDetailsService userDetailsService;
  private final AuthorizedApiService routeService;

  @Autowired
  public DataAccessController(AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      UserDetailsService userDetailsService,
      AuthorizedApiService routeService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.userDetailsService = userDetailsService;
    this.routeService = routeService;
  }

  private UserDetails getUserDetails(String authorizationHeader) throws UsernameNotFoundException {
    Claims body = jwtClaimsParserService.parseToken(authorizationHeader);
    String username = body.getSubject();
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return userDetails;
  }

  private AuthorizedAccessPojo loadApiRouteAccess(String authorizationHeader, String apiRoute) throws UsernameNotFoundException {
    UserDetails userDetails = getUserDetails(authorizationHeader);
    Collection<String> permissions = routeService.getAuthorizedApiRouteAccess(userDetails, apiRoute);
    AuthorizedAccessPojo target = new AuthorizedAccessPojo();
    target.setPermissions(permissions);
    return target;
  }

  @GetMapping("")
  public AuthorizedAccessPojo getApiRoutesAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      UserDetails userDetails = getUserDetails(authorizationHeader);
      Collection<String> routes = routeService.getAuthorizedApiRoutes(userDetails);
      AuthorizedAccessPojo target = new AuthorizedAccessPojo();
      target.setRoutes(routes);
      return target;
    }
    return null;
  }

  @GetMapping("/{apiRoute}")
  public AuthorizedAccessPojo getApiResourceAccess(@RequestHeader HttpHeaders requestHeaders,
      @PathVariable String apiRoute) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, apiRoute);
      return target;
    }
    return null;
  }
}
