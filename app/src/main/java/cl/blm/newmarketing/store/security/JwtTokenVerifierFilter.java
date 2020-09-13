package cl.blm.newmarketing.store.security;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.google.common.base.Strings;

import cl.blm.newmarketing.store.config.JwtProperties;

public class JwtTokenVerifierFilter
    extends OncePerRequestFilter {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;

  public JwtTokenVerifierFilter(SecretKey secretKey, JwtProperties jwtProperties) {
    super();
    this.secretKey = secretKey;
    this.jwtProperties = jwtProperties;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());

    if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtProperties.getTokenPrefix())) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");

    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    } catch (JwtException e) {
      filterChain.doFilter(request, response);
      return;
    }
  }

}
