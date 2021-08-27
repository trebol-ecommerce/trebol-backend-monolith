package org.trebol.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.trebol.exceptions.CorsMappingParseException;

/**
 * CorsConfigurationSource factory class.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CorsConfigurationSourceBuilder {

  private final List<String> allowedHeaders;
  private final List<String> allowedOrigins;
  private final Map<String, String> corsMappings;

  public CorsConfigurationSourceBuilder(CorsProperties corsProperties) throws CorsMappingParseException {
    this.allowedHeaders = corsProperties.getAllowedHeadersAsList();
    this.allowedOrigins = corsProperties.getAllowedOriginsAsList();
    this.corsMappings = corsProperties.getMappingsAsMap();
  }

  public CorsConfigurationSource build() {
    CorsConfiguration baseConfig = new CorsConfiguration();
    baseConfig.setAllowedHeaders(allowedHeaders);
    baseConfig.setAllowCredentials(true);
    baseConfig.setMaxAge(300L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    for (String path : corsMappings.keySet()) {

      List<String> methods = new ArrayList<>();
      for (String m : corsMappings.get(path).split(",")) {
        methods.add(m);
      }
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(allowedOrigins);
      pathConfig.setAllowedMethods(methods);
      source.registerCorsConfiguration(path, pathConfig);
    }
    return source;
  }

}
