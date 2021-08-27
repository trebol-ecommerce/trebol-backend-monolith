package org.trebol.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


public class JwtTokenVerifierFilter
  extends OncePerRequestFilter {

  private final Logger myLogger = LoggerFactory.getLogger(JwtTokenVerifierFilter.class);
  private final IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  public JwtTokenVerifierFilter(IAuthorizationHeaderParserService<Claims> jwtClaimsParserService) {
    super();
    this.jwtClaimsParserService = jwtClaimsParserService;
  }

  private Set<SimpleGrantedAuthority> extractAuthorities(Claims tokenBody) {
    @SuppressWarnings("unchecked")
    List<Map<String, String>> jwsAuthorityMap = (List<Map<String, String>>) tokenBody.get("authorities");
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    for (Map<String, String> authorityKeyValuePair : jwsAuthorityMap) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityKeyValuePair.get("authority"));
      authorities.add(authority);
    }
    return authorities;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, IllegalStateException {

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null || authorizationHeader.isBlank()) {
      filterChain.doFilter(request, response);
    } else {
      String jwt = authorizationHeader.replace("Bearer ", "");
      try {
        Claims tokenBody = jwtClaimsParserService.parseToken(jwt);
        Instant expiration = tokenBody.getExpiration().toInstant();
        Instant now = Instant.now();
        if (expiration.isAfter(now)) {
          String username = tokenBody.getSubject();
          Set<SimpleGrantedAuthority> authorities = this.extractAuthorities(tokenBody);
          Authentication authentication = new UsernamePasswordAuthenticationToken(
              username,
              null,
              authorities);

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
      } catch (NullPointerException | IllegalStateException exc) {
        myLogger.info("Access denied: '{}' '{}' used an invalid token '{}'", request.getMethod(), request.getRequestURI(), jwt);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }
  }

}
