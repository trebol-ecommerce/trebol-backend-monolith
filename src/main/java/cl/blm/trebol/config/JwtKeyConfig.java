package cl.blm.trebol.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtKeyConfig {
  @Autowired
  private SecurityProperties jwtProperties;

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getJwtSecretKey().getBytes());
  }
}
