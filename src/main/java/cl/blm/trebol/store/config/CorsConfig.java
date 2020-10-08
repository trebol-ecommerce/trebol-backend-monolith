package cl.blm.trebol.store.config;

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

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/*").allowedOrigins("http://localhost:4200") // angular app
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE").exposedHeaders("Authorization");
  }

}
