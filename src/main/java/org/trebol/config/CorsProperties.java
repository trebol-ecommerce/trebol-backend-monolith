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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.trebol.exceptions.CorsMappingParseException;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        throw new CorsMappingParseException(
            "Could not parse '" + m + "', format must be 'METHOD[,METHOD2,...] /path/to/api'");
      }
    }
    return map;
  }

}
