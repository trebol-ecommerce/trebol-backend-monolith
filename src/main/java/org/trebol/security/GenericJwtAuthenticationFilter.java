package org.trebol.security;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.trebol.config.SecurityProperties;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Date;

/**
 * Base class for filters that create JWTs for authentication and authorization purposes
 * Only implements successfulAuthentication(), which returns said JWT in the AUTHORIZATION header and response body
 */
public abstract class GenericJwtAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {

  private final SecurityProperties jwtProperties;
  private final SecretKey secretKey;

  public GenericJwtAuthenticationFilter(SecurityProperties jwtProperties, SecretKey secretKey) {
    this.jwtProperties = jwtProperties;
    this.secretKey = secretKey;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    FilterChain chain, Authentication authResult)
    throws IOException {
    int minutesToExpire = jwtProperties.getJwtExpirationAfterMinutes();
    int hoursToExpire = jwtProperties.getJwtExpirationAfterHours();
    int daysToExpire = jwtProperties.getJwtExpirationAfterDays();

    Instant now = Instant.now();
    Instant expiration = now.plus(Period.ofDays(daysToExpire))
        .plus(Duration.ofHours(hoursToExpire))
        .plus(Duration.ofMinutes(minutesToExpire));

    String token = Jwts.builder()
        .setSubject(authResult.getName())
        .claim("authorities", authResult.getAuthorities())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiration))
        .signWith(secretKey)
        .compact();

    String headerValue = "Bearer " + token;
    response.addHeader(HttpHeaders.AUTHORIZATION, headerValue);
    response.getWriter().write(headerValue);
  }
}
