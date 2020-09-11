package cl.blm.newmarketing.store.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for CORS headers
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
public abstract class CorsConfig
    implements WebMvcConfigurer {
  private Logger LOG = LoggerFactory.getLogger(CorsConfig.class);

  // CORS configuration
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    LOG.debug("addCorsMappings");
    registry.addMapping("/api/*").allowedOrigins("http://localhost:4200") // angular app
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE").exposedHeaders("Authorization");
  }

}
