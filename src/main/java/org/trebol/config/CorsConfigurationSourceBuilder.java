package org.trebol.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * CorsConfigurationSource factory class.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CorsConfigurationSourceBuilder {

  private final Logger logger = LoggerFactory.getLogger(CorsConfigurationSourceBuilder.class);

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
    corsMappings.putAll(parseMappings(corsProperties.getMappings()));
  }

  private Map<String, String> parseMappings(String mappings) throws Error {
    Map<String, String> map = new HashMap<>();

    if (logger.isDebugEnabled()) {
      logger.debug("CORS: Note that HEAD and OPTIONS methods are internally included for every mapping", mappings);
      logger.info("CORS: Reading mappings for raw metadata '{}'", mappings);
    } else {
      logger.info("CORS: Reading mappings...");
    }
    for (String m : Arrays.asList(mappings.split(MAPPING_SEPARATOR))) {
      logger.trace("CORS: Processing raw mapping '{}'", m);
      String[] mapping = m.split(" ");
      try {
        String method = mapping[0] + ",HEAD,OPTIONS";
        String path = mapping[1];
        map.put(path, method);
        logger.debug("CORS: Mapping added on Path:'{}', Method(s):'{}'", path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new Error("CORS: Could not parse mapping, format must be 'METHODS /path'");
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
