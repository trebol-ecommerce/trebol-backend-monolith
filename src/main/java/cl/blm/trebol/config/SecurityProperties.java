package cl.blm.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.net.HttpHeaders;

@Configuration
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

  private String jwtSecretKey;
  private String jwtTokenPrefix;
  private Integer jwtTokenExpirationAfterDays;
  private Integer bcryptEncoderStrength;

  public SecurityProperties() {
  }

  public String getJwtSecretKey() {
    return jwtSecretKey;
  }

  public void setJwtSecretKey(String jwtSecretKey) {
    this.jwtSecretKey = jwtSecretKey;
  }

  public String getJwtTokenPrefix() {
    return jwtTokenPrefix;
  }

  public void setJwtTokenPrefix(String jwtTokenPrefix) {
    this.jwtTokenPrefix = jwtTokenPrefix;
  }

  public Integer getJwtTokenExpirationAfterDays() {
    return jwtTokenExpirationAfterDays;
  }

  public void setJwtTokenExpirationAfterDays(Integer jwtTokenExpirationAfterDays) {
    this.jwtTokenExpirationAfterDays = jwtTokenExpirationAfterDays;
  }

  public String getAuthorizationHeader() {
    return HttpHeaders.AUTHORIZATION;
  }

  public Integer getBcryptEncoderStrength() {
    return bcryptEncoderStrength;
  }

  public void setBcryptEncoderStrength(Integer bcryptEncoderStrength) {
    this.bcryptEncoderStrength = bcryptEncoderStrength;
  }
}
