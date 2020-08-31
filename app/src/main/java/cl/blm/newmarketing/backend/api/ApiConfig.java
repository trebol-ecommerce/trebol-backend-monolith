package cl.blm.newmarketing.backend.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for API-related settings and beans.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
public abstract class ApiConfig
    implements WebMvcConfigurer {
  private Logger LOG = LoggerFactory.getLogger(ApiConfig.class);

  // JSON parser / serializer
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  // CORS configuration
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    LOG.debug("addCorsMappings");
    registry.addMapping("/api/*").allowedOrigins("http://localhost:4200") // angular app
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE").exposedHeaders("Authorization");
  }

}
