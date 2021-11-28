package org.trebol.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtKeyConfig {

  @Bean
  public SecretKey secretKey(SecurityProperties jwtProperties) {
    return Keys.hmacShaKeyFor(jwtProperties.getJwtSecretKey().getBytes());
  }
}
