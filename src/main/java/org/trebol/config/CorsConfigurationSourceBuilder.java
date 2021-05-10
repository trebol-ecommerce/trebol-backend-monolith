package org.trebol.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  public static final String ORIGIN_SEPARATOR = ";";
  public static final String MAPPING_SEPARATOR = ";";

  private final List<String> origins;
  private final Map<String, String> corsMappings;

  public CorsConfigurationSourceBuilder(CorsProperties corsProperties) {
    this.origins = new ArrayList<>();
    for (String o : corsProperties.getOrigins().split(ORIGIN_SEPARATOR)) {
      origins.add(o);
    }

    this.corsMappings = new HashMap<>();
    corsMappings.putAll(parseMappings(corsProperties.getDefaultMapping()));
    corsMappings.putAll(parseMappings(corsProperties.getStoreMappings()));
    corsMappings.putAll(parseMappings(corsProperties.getSessionMappings()));
    corsMappings.putAll(parseMappings(corsProperties.getAccessMappings()));
    corsMappings.putAll(parseMappings(corsProperties.getDataMappings()));
  }

  private Map<String, String> parseMappings(String mappings) throws Error {
    Map<String, String> map = new HashMap<>();

    for (String m : Arrays.asList(mappings.split(MAPPING_SEPARATOR))) {
      String[] mapping = m.split(" ");
      try {
        String method = mapping[0] + ",HEAD,OPTIONS";
        String path = mapping[1];
        map.put(path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new Error("Could not parse CORS mapping, format must be 'METHODS /path'");
      }
    }
    return map;
  }

  public CorsConfigurationSource build() {
    CorsConfiguration baseConfig = new CorsConfiguration();
    baseConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "X-Requested-With", "Authorization"));
    baseConfig.setAllowCredentials(true);
    baseConfig.setMaxAge(300L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    for (String path : corsMappings.keySet()) {

      List<String> methods = new ArrayList<>();
      for (String m : corsMappings.get(path).split(",")) {
        methods.add(m);
      }
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(origins);
      pathConfig.setAllowedMethods(methods);
      source.registerCorsConfiguration(path, pathConfig);
    }
    return source;
  }

}
