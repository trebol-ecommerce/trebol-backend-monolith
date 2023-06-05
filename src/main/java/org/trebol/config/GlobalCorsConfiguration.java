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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class GlobalCorsConfiguration {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalCorsConfiguration.class);

	private final String[] allowedHeaders;
	private final String[] allowedOrigins;

	@Autowired
	public GlobalCorsConfiguration(CorsProperties corsProperties) {
		this.allowedHeaders = corsProperties.getAllowedHeaders().split(corsProperties.getListDelimiter());
		this.allowedOrigins = corsProperties.getAllowedOrigins().split(corsProperties.getListDelimiter());
    LOGGER.debug("allowedOrigins={}", Arrays.asList(this.allowedOrigins));
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        	@Override
        	public void addCorsMappings(CorsRegistry registry) {
        		registry.addMapping("/public/login")
        		   	.allowedOrigins(allowedOrigins)
        		   	.allowedHeaders(allowedHeaders)
        		   	.allowedMethods("POST");

        		registry.addMapping("/public/guest")
        			.allowedOrigins(allowedOrigins)
        			.allowedHeaders(allowedHeaders)
        			.allowedMethods("POST");

        		registry.addMapping("/**")
        			.allowedOrigins(allowedOrigins)
        			.allowedHeaders(allowedHeaders)
        			.allowCredentials(true)
        			.maxAge(300L)
        			.allowedMethods("");
            }
        };
    }

}
