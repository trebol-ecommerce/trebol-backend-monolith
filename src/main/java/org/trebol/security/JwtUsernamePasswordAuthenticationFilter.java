package org.trebol.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;

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

  private final AuthenticationManager authenticationManager;
  private final SecurityProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtUsernamePasswordAuthenticationFilter(
      AuthenticationManager authenticationManager,
      SecurityProperties jwtProperties,
      SecretKey secretKey) {
    this.authenticationManager = authenticationManager;
    this.jwtProperties = jwtProperties;
    this.secretKey = secretKey;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    try {
      UsernamePasswordPojo authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
          UsernamePasswordPojo.class);
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          authenticationRequest.getName(),
          authenticationRequest.getPassword());
      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    String token = Jwts.builder()
        .setSubject(authResult.getName())
        .claim("authorities", authResult.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getJwtTokenExpirationAfterDays())))
        .signWith(secretKey)
        .compact();

    String headerValue = jwtProperties.getJwtTokenPrefix() + token;
    response.addHeader(jwtProperties.getAuthorizationHeader(), headerValue);
    response.getWriter().write(headerValue);
  }
}
