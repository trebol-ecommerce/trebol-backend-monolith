package cl.blm.trebol.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

  private final String separator = ";";

  private String origins;
  private String defaultMapping;
  private String storeMappings;
  private String sessionMappings;
  private String accessMappings;
  private String dataMappings;

  public CorsProperties() {
  }

  private Map<String, String> parseMappings(String mappings) throws Error {
    Map<String, String> map = new HashMap<>();

    for (String m : Arrays.asList(mappings.split(separator))) {
      String[] mapping = m.split(" ");
      try {
        String method = mapping[0];
        String path = mapping[1];
        map.put(path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new Error("Could not parse CORS mapping, format must be 'METHODS /path'");
      }
    }
    return map;
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

  public Map<String, String> getStoreMappingsAsMap() {
    return parseMappings(storeMappings);
  }

  public String getSessionMappings() {
    return sessionMappings;
  }

  public void setSessionMappings(String sessionMappings) {
    this.sessionMappings = sessionMappings;
  }

  public Map<String,String> getSessionMappingsAsMap() {
    return parseMappings(sessionMappings);
  }

  public String getAccessMappings() {
    return accessMappings;
  }

  public void setAccessMappings(String accessMappings) {
    this.accessMappings = accessMappings;
  }

  public Map<String, String> getAccessMappingsAsMap() {
    return parseMappings(accessMappings);
  }

  public String getDataMappings() {
    return dataMappings;
  }

  public void setDataMappings(String dataMappings) {
    this.dataMappings = dataMappings;
  }

  public Map<String, String> getDataMappingsAsMap() {
    return parseMappings(dataMappings);
  }

}
