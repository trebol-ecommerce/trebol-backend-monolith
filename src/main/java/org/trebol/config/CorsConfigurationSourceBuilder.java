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
import java.util.List;
import java.util.Map;

/**
 * CorsConfigurationSource builder class
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

      List<String> methods = Arrays.asList(corsMappings.get(path).split(","));
      CorsConfiguration pathConfig = new CorsConfiguration(baseConfig);
      pathConfig.setAllowedOrigins(allowedOrigins);
      pathConfig.setAllowedMethods(methods);
      source.registerCorsConfiguration(path, pathConfig);
    }
    return source;
  }

}
