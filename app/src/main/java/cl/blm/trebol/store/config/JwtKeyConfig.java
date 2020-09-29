package cl.blm.trebol.store.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtKeyConfig {
  @Autowired
  private JwtProperties jwtProperties;

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
  }
}
