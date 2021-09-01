package org.trebol.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.AuthorizedAccessPojo;
import org.trebol.security.IAuthorizedApiService;
import org.trebol.security.IAuthorizationHeaderParserService;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/access")
public class AccessController {

  private final IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final UserDetailsService userDetailsService;
  private final IAuthorizedApiService routeService;

  @Autowired
  public AccessController(IAuthorizationHeaderParserService<Claims> jwtClaimsParserService,
    UserDetailsService userDetailsService, IAuthorizedApiService routeService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.userDetailsService = userDetailsService;
    this.routeService = routeService;
  }

  private UserDetails getUserDetails(HttpHeaders requestHeaders)
    throws UsernameNotFoundException, IllegalStateException {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader == null || !authorizationHeader.matches("^Bearer .+$")) {
      return null;
    } else {
      String token = authorizationHeader.replace("Bearer ", "");
      Claims body = jwtClaimsParserService.parseToken(token);
      String username = body.getSubject();
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      return userDetails;
    }
  }

  @GetMapping({"", "/"})
  public AuthorizedAccessPojo getApiRoutesAccess(@RequestHeader HttpHeaders requestHeaders)
    throws UsernameNotFoundException, IllegalStateException {
    UserDetails userDetails = this.getUserDetails(requestHeaders);
    if (userDetails == null) {
      return null;
    } else {
      Collection<String> routes = routeService.getAuthorizedApiRoutes(userDetails);
      AuthorizedAccessPojo target = new AuthorizedAccessPojo();
      target.setRoutes(routes);
      return target;
    }
  }

  @GetMapping({"/{apiRoute}", "/{apiRoute}/"})
  public AuthorizedAccessPojo getApiResourceAccess(
      @RequestHeader HttpHeaders requestHeaders,
      @PathVariable String apiRoute)
    throws UsernameNotFoundException, IllegalStateException {
    UserDetails userDetails = this.getUserDetails(requestHeaders);
    if (userDetails == null) {
      return null;
    } else {
      Collection<String> permissions = routeService.getAuthorizedApiRouteAccess(userDetails, apiRoute);
      AuthorizedAccessPojo target = new AuthorizedAccessPojo();
      target.setPermissions(permissions);
      return target;
    }
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(UsernameNotFoundException.class)
  public void handleException(UsernameNotFoundException ex) { }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(IllegalStateException.class)
  public void handleException(IllegalStateException ex) { }
  
}
