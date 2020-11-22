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

  public static final String MAPPING_SEPARATOR = ";";

  private final String origins;
  private final Map<String, String> corsMappings;

  public CorsConfigurationSourceBuilder(CorsProperties corsProperties) {
    this.origins = corsProperties.getOrigins();
    this.corsMappings = new HashMap<>();
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
      String methods = corsMappings.get(path);
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(Arrays.asList(origins));
      pathConfig.setAllowedMethods(Arrays.asList(methods.split(",")));
      source.registerCorsConfiguration(path, pathConfig);
    }
    return source;
  }

}
