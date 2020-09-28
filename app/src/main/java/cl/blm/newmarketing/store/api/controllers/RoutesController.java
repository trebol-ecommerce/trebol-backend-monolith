package cl.blm.newmarketing.store.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.store.api.pojo.AuthorizedAccessPojo;
import cl.blm.newmarketing.store.services.RouteService;
import cl.blm.newmarketing.store.services.security.AuthorizationTokenParserService;

@RestController
public class RoutesController {

  private final AuthorizationTokenParserService<Claims> jwtClaimsParserService;
  private final UserDetailsService userDetailsService;
  private final RouteService routeService;

  @Autowired
  public RoutesController(AuthorizationTokenParserService<Claims> jwtClaimsParserService,
      UserDetailsService userDetailsService,
      RouteService routeService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.userDetailsService = userDetailsService;
    this.routeService = routeService;
  }

  @GetMapping("/api")
  public AuthorizedAccessPojo getApiRoutes(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);

    if (authorizationHeader != null) {
      Claims body = jwtClaimsParserService.parseToken(authorizationHeader);
      String username = body.getSubject();
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Collection<String> routes = routeService.getAuthorizedApiRoutes(userDetails);
      AuthorizedAccessPojo target = new AuthorizedAccessPojo();
      target.setRoutes(routes);
      return target;
    }
    return null;
  }
}
