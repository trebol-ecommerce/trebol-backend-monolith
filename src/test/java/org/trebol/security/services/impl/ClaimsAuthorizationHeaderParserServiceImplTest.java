package org.trebol.security.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.trebol.security.SecurityTestingConfig;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityTestingConfig.class)
public class ClaimsAuthorizationHeaderParserServiceImplTest {
  ClaimsAuthorizationHeaderParserServiceImpl instance;
  @Autowired SecretKey secretKey;

  @BeforeEach
  void beforeEach() {
    instance = new ClaimsAuthorizationHeaderParserServiceImpl(secretKey);
  }

  @Test
  void parses_jwts() {
    Date thisInstant = Date.from(Instant.now());
    String token = Jwts.builder()
      .setIssuedAt(thisInstant)
      .signWith(secretKey)
      .compact();
    Claims claims = instance.parseToken(token);
    assertEquals(thisInstant.toString(), claims.getIssuedAt().toString());
  }

  @Test
  void extracts_authorization_header() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(ANY);
    String authorizationHeader = instance.extractAuthorizationHeader(headers);
    assertEquals("Bearer " + ANY, authorizationHeader);
  }
}
