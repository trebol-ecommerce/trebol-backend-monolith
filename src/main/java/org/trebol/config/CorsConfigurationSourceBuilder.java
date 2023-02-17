/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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
import org.trebol.config.exceptions.CorsMappingParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates an instance of CorsConfigurationSource using information from an instance of CorsProperties
 */
public class CorsConfigurationSourceBuilder {
  private static final Long MAX_AGE = 300L;
  private final CorsConfiguration config;
  private final String listDelimiter;
  private final Map<String, String> mappings;

  public CorsConfigurationSourceBuilder(String listDelimiter) {
    this.listDelimiter = listDelimiter;
    this.config = new CorsConfiguration();
    this.config.setAllowCredentials(true);
    this.config.setMaxAge(MAX_AGE);
    this.mappings = new HashMap<>();
  }

  public CorsConfigurationSourceBuilder allowedHeaders(String allowedHeadersString) {
    List<String> headersList = Arrays.asList(allowedHeadersString.split(this.listDelimiter));
    this.config.setAllowedHeaders(headersList);
    return this;
  }

  public CorsConfigurationSourceBuilder allowedOrigins(String allowedOriginsString) {
    List<String> originsList = Arrays.asList(allowedOriginsString.split(this.listDelimiter));
    this.config.setAllowedOrigins(originsList);
    return this;
  }

  public CorsConfigurationSourceBuilder corsMappings(String corsMappings) throws CorsMappingParseException {
    for (String chunk : corsMappings.split(this.listDelimiter)) {
      String[] mapping = chunk.split(" ");
      try {
        String method = mapping[0] + ",HEAD,OPTIONS";
        String path = mapping[1];
        this.mappings.put(path, method);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new CorsMappingParseException(chunk);
      }
    }
    return this;
  }

  public CorsConfigurationSource build() {
    UrlBasedCorsConfigurationSource cfg = new UrlBasedCorsConfigurationSource();
    for (Map.Entry<String, String> properties : mappings.entrySet()) {
      List<String> methods = Arrays.asList(properties.getValue().split(","));
      CorsConfiguration pathConfig = new CorsConfiguration(config);
      pathConfig.setAllowedMethods(methods);
      cfg.registerCorsConfiguration(properties.getKey(), pathConfig);
    }
    return cfg;
  }
}
