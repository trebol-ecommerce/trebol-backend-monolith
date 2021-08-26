package org.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

  private String origins;
  private String mappings;

  public CorsProperties() {
  }

  public String getOrigins() {
    return origins;
  }

  public void setOrigins(String origins) {
    this.origins = origins;
  }

  public String getMappings() {
    return mappings;
  }

  public void setMappings(String mappings) {
    this.mappings = mappings;
  }

}
