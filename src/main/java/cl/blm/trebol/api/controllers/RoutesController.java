package cl.blm.trebol.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.AuthorizedAccessPojo;
import cl.blm.trebol.services.security.AuthorizationHeaderParserService;
import cl.blm.trebol.services.security.AuthorizedApiService;

@RestController
public class RoutesController {

  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final UserDetailsService userDetailsService;
  private final AuthorizedApiService routeService;

  @Autowired
  public RoutesController(AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
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

  @GetMapping("/api")
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

  @GetMapping("/api/client")
  public AuthorizedAccessPojo getClientApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "clients");
      return target;
    }
    return null;
  }

  @GetMapping("/api/person")
  public AuthorizedAccessPojo getPersonApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "people");
      return target;
    }
    return null;
  }

  @GetMapping("/api/product")
  public AuthorizedAccessPojo getProductApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "products");
      return target;
    }
    return null;
  }

  @GetMapping("/api/product_type")
  public AuthorizedAccessPojo getProductTypeApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "product_types");
      return target;
    }
    return null;
  }

  @GetMapping("/api/product_family")
  public AuthorizedAccessPojo getProductFamilyApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "product_families");
      return target;
    }
    return null;
  }

  @GetMapping("/api/sell")
  public AuthorizedAccessPojo getSellApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "sales");
      return target;
    }
    return null;
  }

  @GetMapping("/api/sell_type")
  public AuthorizedAccessPojo getSellTypeApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "sell_types");
      return target;
    }
    return null;
  }

  @GetMapping("/api/seller")
  public AuthorizedAccessPojo getSellerApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "sellers");
      return target;
    }
    return null;
  }

  @GetMapping("/api/user")
  public AuthorizedAccessPojo getUserApiAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
    if (authorizationHeader != null) {
      AuthorizedAccessPojo target = loadApiRouteAccess(authorizationHeader, "users");
      return target;
    }
    return null;
  }
}
