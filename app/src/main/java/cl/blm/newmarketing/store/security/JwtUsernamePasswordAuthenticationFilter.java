package cl.blm.newmarketing.store.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.blm.newmarketing.store.config.JwtProperties;
import cl.blm.newmarketing.store.security.pojo.UsernamePasswordPojo;

public class JwtUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtUsernamePasswordAuthenticationFilter(
      AuthenticationManager authenticationManager,
      JwtProperties jwtProperties,
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
          authenticationRequest.getUsername(),
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
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getTokenExpirationAfterDays())))
        .signWith(secretKey)
        .compact();

    String headerValue = jwtProperties.getTokenPrefix() + token;
    response.addHeader(jwtProperties.getAuthorizationHeader(), headerValue);
  }
}
