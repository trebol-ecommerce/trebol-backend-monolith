package cl.blm.newmarketing.store.security;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.blm.newmarketing.store.security.pojo.UsernamePasswordPojo;
import io.jsonwebtoken.Jwts;

public class JwtUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      UsernamePasswordPojo authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
          UsernamePasswordPojo.class);
      Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
          authenticationRequest.getPassword());
      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    Key key = JwtKeyGenerator.SECRET_KEY;
    String token = Jwts.builder().setSubject(authResult.getName()).claim("authorities", authResult.getAuthorities())
        .setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1))).signWith(key)
        .compact();

    response.addHeader("Authorization", "Bearer " + token);
  }
}
