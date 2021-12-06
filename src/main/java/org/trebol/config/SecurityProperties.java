package org.trebol.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.security")
public class SecurityProperties {

  @NotBlank
  private String jwtSecretKey;
  @PositiveOrZero
  private int jwtExpirationAfterMinutes;
  @PositiveOrZero
  private int jwtExpirationAfterHours;
  @PositiveOrZero
  private int jwtExpirationAfterDays;
  @Min(6)
  private int bcryptEncoderStrength;

  public String getJwtSecretKey() {
    return jwtSecretKey;
  }

  public void setJwtSecretKey(String jwtSecretKey) {
    this.jwtSecretKey = jwtSecretKey;
  }

  public int getJwtExpirationAfterDays() {
    return jwtExpirationAfterDays;
  }

  public void setJwtExpirationAfterDays(int jwtExpirationAfterDays) {
    this.jwtExpirationAfterDays = jwtExpirationAfterDays;
  }

  public int getJwtExpirationAfterMinutes() {
    return jwtExpirationAfterMinutes;
  }

  public void setJwtExpirationAfterMinutes(int jwtExpirationAfterMinutes) {
    this.jwtExpirationAfterMinutes = jwtExpirationAfterMinutes;
  }

  public int getJwtExpirationAfterHours() {
    return jwtExpirationAfterHours;
  }

  public void setJwtExpirationAfterHours(int jwtExpirationAfterHours) {
    this.jwtExpirationAfterHours = jwtExpirationAfterHours;
  }

  public int getBcryptEncoderStrength() {
    return bcryptEncoderStrength;
  }

  public void setBcryptEncoderStrength(int bcryptEncoderStrength) {
    this.bcryptEncoderStrength = bcryptEncoderStrength;
  }
}
