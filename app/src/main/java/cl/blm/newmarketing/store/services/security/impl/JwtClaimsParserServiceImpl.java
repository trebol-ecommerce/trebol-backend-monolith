package cl.blm.newmarketing.store.services.security.impl;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import cl.blm.newmarketing.store.config.JwtProperties;
import cl.blm.newmarketing.store.services.security.AuthorizationTokenParserService;

@Service
public class JwtClaimsParserServiceImpl
    implements AuthorizationTokenParserService<Claims> {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;

  @Autowired
  public JwtClaimsParserServiceImpl(SecretKey secretKey, JwtProperties jwtProperties) {
    this.secretKey = secretKey;
    this.jwtProperties = jwtProperties;
  }

  @Override
  public String extractAuthorizationHeaderFromRequest(HttpServletRequest request) {
    return request.getHeader(jwtProperties.getAuthorizationHeader());
  }

  @Override
  public Claims parseToken(String authorizationHeader) throws IllegalStateException {
    String token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");
    try {
      Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token);

      Claims body = claimsJws.getBody();
      return body;
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s can't be trusted", token));
    }
  }

  @Nullable
  @Override
  public String extractAuthorizationHeader(HttpHeaders httpHeaders) {
    String authHeaderKey = jwtProperties.getAuthorizationHeader();
    if (httpHeaders.containsKey(authHeaderKey)) {
      String value = httpHeaders.getFirst(authHeaderKey);
      return value;
    } else {
      return null;
    }
  }

}
