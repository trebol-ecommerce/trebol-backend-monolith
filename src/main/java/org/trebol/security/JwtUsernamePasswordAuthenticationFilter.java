package org.trebol.security;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.trebol.config.SecurityProperties;
import org.trebol.api.pojo.UsernamePasswordPojo;

public class JwtUsernamePasswordAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {

  private final Logger myLogger = LoggerFactory.getLogger(JwtUsernamePasswordAuthenticationFilter.class);
  private final AuthenticationManager authenticationManager;
  private final SecurityProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
    SecurityProperties jwtProperties, SecretKey secretKey) {
    this.authenticationManager = authenticationManager;
    this.jwtProperties = jwtProperties;
    this.secretKey = secretKey;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
    if (!HttpMethod.POST.matches(request.getMethod())) {
      return null;
    } else {
      try {
        UsernamePasswordPojo authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
            UsernamePasswordPojo.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            authenticationRequest.getName(),
            authenticationRequest.getPassword());
        return authenticationManager.authenticate(authentication);
      } catch (IOException e) {
        myLogger.error("There was a problem while reading the login request", e);
        throw new RuntimeException("El servidor no pudo procesar la solicitud correctamente");
      }
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    FilterChain chain, Authentication authResult)
    throws IOException, ServletException {
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
