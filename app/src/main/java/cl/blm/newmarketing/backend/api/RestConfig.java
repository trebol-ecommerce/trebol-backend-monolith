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
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
public abstract class RestConfig
    implements WebMvcConfigurer {
  private Logger LOG = LoggerFactory.getLogger(RestConfig.class);

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    LOG.debug("addCorsMappings");
    registry.addMapping("/api/*").allowedOrigins("http://localhost:4200") // angular app
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE").exposedHeaders("Authorization");
  }

}
