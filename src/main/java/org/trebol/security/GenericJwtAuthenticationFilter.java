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

import static org.trebol.config.Constants.JWT_CLAIM_AUTHORITIES;
import static org.trebol.config.Constants.JWT_PREFIX;

/**
 * Abstract filter that writes a Bearer token to the response upon a succesful authentication call
 */
public abstract class GenericJwtAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {
  private final SecurityProperties jwtProperties;
  private final SecretKey secretKey;

  protected GenericJwtAuthenticationFilter(
    SecurityProperties jwtProperties,
    SecretKey secretKey
  ) {
    this.jwtProperties = jwtProperties;
    this.secretKey = secretKey;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult)
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
      .claim(JWT_CLAIM_AUTHORITIES, authResult.getAuthorities())
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(expiration))
      .signWith(secretKey)
      .compact();

    String headerValue = JWT_PREFIX + token;
    response.addHeader(HttpHeaders.AUTHORIZATION, headerValue);
    response.getWriter().write(headerValue);
  }
}
