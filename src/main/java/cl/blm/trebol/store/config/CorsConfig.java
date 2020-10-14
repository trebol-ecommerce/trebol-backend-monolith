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
// uncomment and adjust as required
//    registry.addMapping("/**")
//        .allowedOrigins("*")
//        .allowedMethods("GET,POST,PUT,DELETE,OPTIONS")
//        .exposedHeaders("Content-Type,X-Amz-Date,Authorization,X-Api-Key,x-requested-with");
  }

}
