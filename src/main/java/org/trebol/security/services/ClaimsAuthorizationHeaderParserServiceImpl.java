package org.trebol.security.services;

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

import org.trebol.security.IAuthorizationHeaderParserService;

@Service
public class ClaimsAuthorizationHeaderParserServiceImpl
    implements IAuthorizationHeaderParserService<Claims> {

  private final SecretKey secretKey;

  @Autowired
  public ClaimsAuthorizationHeaderParserServiceImpl(SecretKey secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public Claims parseToken(String token) throws IllegalStateException {
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
    String authHeaderKey = HttpHeaders.AUTHORIZATION;
    if (httpHeaders.containsKey(authHeaderKey)) {
      String value = httpHeaders.getFirst(authHeaderKey);
      return value;
    } else {
      return null;
    }
  }

}
