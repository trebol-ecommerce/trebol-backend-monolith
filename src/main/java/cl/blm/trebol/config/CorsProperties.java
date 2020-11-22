package cl.blm.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

  private String origins;
  private String defaultMapping;
  private String storeMappings;
  private String sessionMappings;
  private String accessMappings;
  private String dataMappings;

  public CorsProperties() {
  }

  public String getOrigins() {
    return origins;
  }

  public void setOrigins(String origins) {
    this.origins = origins;
  }

  public String getDefaultMapping() {
    return defaultMapping;
  }

  public void setDefaultMapping(String defaultMapping) {
    this.defaultMapping = defaultMapping;
  }

  public String getStoreMappings() {
    return storeMappings;
  }

  public void setStoreMappings(String storeMappings) {
    this.storeMappings = storeMappings;
  }

  public String getSessionMappings() {
    return sessionMappings;
  }

  public void setSessionMappings(String sessionMappings) {
    this.sessionMappings = sessionMappings;
  }

  public String getAccessMappings() {
    return accessMappings;
  }

  public void setAccessMappings(String accessMappings) {
    this.accessMappings = accessMappings;
  }

  public String getDataMappings() {
    return dataMappings;
  }

  public void setDataMappings(String dataMappings) {
    this.dataMappings = dataMappings;
  }

}
