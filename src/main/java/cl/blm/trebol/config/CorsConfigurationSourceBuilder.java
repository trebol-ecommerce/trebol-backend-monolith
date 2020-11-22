package cl.blm.trebol.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CorsConfigurationSource factory class.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CorsConfigurationSourceBuilder {

  private final String origins;
  private final Map<String, String> corsMappings;

  public CorsConfigurationSourceBuilder(CorsProperties corsProperties) {
    this.origins = corsProperties.getOrigins();
    this.corsMappings = new HashMap<>();
    corsMappings.put("/", "GET,OPTIONS");
    corsMappings.putAll(corsProperties.getStoreMappingsAsMap());
    corsMappings.putAll(corsProperties.getSessionMappingsAsMap());
    corsMappings.putAll(corsProperties.getAccessMappingsAsMap());
    corsMappings.putAll(corsProperties.getDataMappingsAsMap());
  }

  public CorsConfigurationSource build() {
    CorsConfiguration baseConfig = new CorsConfiguration();
    baseConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With", "Authorization"));
    baseConfig.setAllowCredentials(true);
    baseConfig.setMaxAge(300L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    for (String path : corsMappings.keySet()) {
      String methods = corsMappings.get(path);
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(Arrays.asList(origins));
      pathConfig.setAllowedMethods(Arrays.asList(methods.split(",")));
      source.registerCorsConfiguration(path, pathConfig);
    }
    return source;
  }

}
