package org.trebol.security;

import java.io.IOException;
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

import org.trebol.security.services.AuthorizationHeaderParserService;

public class JwtTokenVerifierFilter
    extends OncePerRequestFilter {

  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  public JwtTokenVerifierFilter(AuthorizationHeaderParserService<Claims> jwtClaimsParserService) {
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

    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeaderFromRequest(request);

    if (authorizationHeader != null) {
      Claims tokenBody = jwtClaimsParserService.parseToken(authorizationHeader);
      String username = tokenBody.getSubject();
      Set<SimpleGrantedAuthority> authorities = extractAuthorities(tokenBody);
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          username,
          null,
          authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
