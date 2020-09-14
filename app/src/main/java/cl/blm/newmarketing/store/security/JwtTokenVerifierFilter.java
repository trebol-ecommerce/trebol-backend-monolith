package cl.blm.newmarketing.store.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;
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
import io.jsonwebtoken.Jws;
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

  private Set<SimpleGrantedAuthority> extractAuthorities(Claims body) {
    @SuppressWarnings("unchecked")
    List<Map<String, String>> jwsAuthorityMap = (List<Map<String, String>>) body.get("authorities");
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    for (Map<String, String> authorityKeyValuePair : jwsAuthorityMap) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authorityKeyValuePair.get("authority"));
      authorities.add(authority);
    }
    return authorities;
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
      Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);

      Claims body = claimsJws.getBody();

      String username = body.getSubject();
      Set<SimpleGrantedAuthority> authorities = extractAuthorities(body);
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          username,
          null,
          authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
    }

    filterChain.doFilter(request, response);
  }

}
