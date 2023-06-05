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

package org.trebol.security;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.trebol.config.Constants.JWT_CLAIM_AUTHORITIES;
import static org.trebol.config.Constants.JWT_PREFIX;

public class JwtTokenVerifierFilter
  extends OncePerRequestFilter {
  private final Logger myLogger = LoggerFactory.getLogger(JwtTokenVerifierFilter.class);
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  public JwtTokenVerifierFilter(
    AuthorizationHeaderParserService<Claims> jwtClaimsParserService
  ) {
    super();
    this.jwtClaimsParserService = jwtClaimsParserService;
  }

  private Set<SimpleGrantedAuthority> extractAuthorities(Claims tokenBody) {
    @SuppressWarnings("unchecked")
    List<Map<String, String>> jwsAuthorityMap = (List<Map<String, String>>) tokenBody.get(JWT_CLAIM_AUTHORITIES);
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    for (Map<String, String> authorityKeyValuePair : jwsAuthorityMap) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityKeyValuePair.get("authority"));
      authorities.add(authority);
    }
    return authorities;
  }

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain)
    throws ServletException, IOException, IllegalStateException {

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.isBlank(authorizationHeader)) {
      filterChain.doFilter(request, response);
    } else {
      String jwt = authorizationHeader.replace(JWT_PREFIX, "");
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
        myLogger.info("Access denied: '{}' '{}' used an invalid token '{}'",
          request.getMethod(),
          request.getRequestURI(),
          jwt);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }
  }
}
