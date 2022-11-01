package org.trebol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfiguration {
	
	private final String[] allowedHeaders;
	private final String[] allowedOrigins;
	
	@Autowired
	public GlobalCorsConfiguration(CorsProperties corsProperties) {
		this.allowedHeaders = corsProperties.getAllowedHeaders().split(corsProperties.getListDelimiter());
	    this.allowedOrigins = corsProperties.getAllowedOrigins().split(corsProperties.getListDelimiter());		
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
