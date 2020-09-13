package cl.blm.newmarketing.store.security;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.google.common.base.Strings;

public class JwtTokenVerifierFilter
    extends OncePerRequestFilter {

  @Autowired
  private SecretKey secretKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = request.getHeader("Authorization");

    if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.replace("Bearer ", "");

    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    } catch (JwtException e) {
      filterChain.doFilter(request, response);
      return;
    }
  }

}
