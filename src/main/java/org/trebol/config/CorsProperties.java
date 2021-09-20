package org.trebol.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.trebol.exceptions.CorsMappingParseException;

@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.cors")
public class CorsProperties {

  @NotBlank
  private String allowedHeaders;
  @NotBlank
  private String allowedOrigins;
  @NotBlank
  private String mappings;
  @NotBlank
  private String listDelimiter;

  public CorsProperties() { }

  public String getAllowedHeaders() {
    return allowedHeaders;
  }

  public void setAllowedHeaders(String allowedHeaders) {
    this.allowedHeaders = allowedHeaders;
  }

  public String getAllowedOrigins() {
    return allowedOrigins;
  }

  public void setAllowedOrigins(String allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  public String getMappings() {
    return mappings;
  }

  public void setMappings(String mappings) {
    this.mappings = mappings;
  }

  public String getListDelimiter() {
    return listDelimiter;
  }

  public void setListDelimiter(String listDelimiter) {
    this.listDelimiter = listDelimiter;
  }

  public List<String> getAllowedHeadersAsList() {
    return Arrays.asList(allowedHeaders.split(listDelimiter));
  }

  public List<String> getAllowedOriginsAsList() {
    return Arrays.asList(allowedOrigins.split(listDelimiter));
  }

  public Map<String, String> getMappingsAsMap() throws CorsMappingParseException {
    Map<String, String> map = new HashMap<>();

    for (String m : mappings.split(listDelimiter)) {
      String[] mapping = m.split(" ");
      try {
        String method = mapping[0] + ",HEAD,OPTIONS";
        String path = mapping[1];
        map.put(path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new CorsMappingParseException("Could not parse '" + m + "', format must be 'METHOD[,METHOD2,...] /path/to/api'");
      }
    }
    return map;
  }

}
