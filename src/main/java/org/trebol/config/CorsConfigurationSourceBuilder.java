/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.trebol.exceptions.CorsMappingParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Type that creates an instance of CorsConfigurationSource using information from an instance of CorsProperties
 */
public class CorsConfigurationSourceBuilder {

  private final String listDelimiter;
  private final List<String> allowedHeaders;
  private final List<String> allowedOrigins;
  private final Map<String, String> mappings;

  public CorsConfigurationSourceBuilder(CorsProperties corsProperties) throws CorsMappingParseException {
    this.listDelimiter = corsProperties.getListDelimiter();
    this.allowedHeaders = Arrays.asList(corsProperties.getAllowedHeaders().split(this.listDelimiter));
    this.allowedOrigins = Arrays.asList(corsProperties.getAllowedOrigins().split(this.listDelimiter));
    this.mappings = this.parseMappings(corsProperties);
  }

  public CorsConfigurationSource build() {
    CorsConfiguration baseConfig = new CorsConfiguration();
    baseConfig.setAllowedHeaders(this.allowedHeaders);
    baseConfig.setAllowCredentials(true);
    baseConfig.setMaxAge(300L);
    UrlBasedCorsConfigurationSource cfg = new UrlBasedCorsConfigurationSource();
    for (Map.Entry<String,String> properties : mappings.entrySet()) {
      List<String> methods = Arrays.asList(properties.getValue().split(","));
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(this.allowedOrigins);
      pathConfig.setAllowedMethods(methods);
      cfg.registerCorsConfiguration(properties.getKey(), pathConfig);
    }
    return cfg;
  }

  private Map<String, String> parseMappings(CorsProperties corsProperties) throws CorsMappingParseException {
    Map<String, String> map = new HashMap<>();

    for (String chunk : corsProperties.getMappings().split(this.listDelimiter)) {
      String[] mapping = chunk.split(" ");
      try {
        String method = mapping[0] + ",HEAD,OPTIONS";
        String path = mapping[1];
        map.put(path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new CorsMappingParseException(
                "Could not parse '" + chunk + "', format must be 'METHOD[,METHOD2,...] /path/to/api'");
      }
    }
    return map;
  }

}
