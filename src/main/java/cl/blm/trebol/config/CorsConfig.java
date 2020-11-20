package cl.blm.trebol.config;

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
    this.addStoreCorsMappings(registry);
    this.addSessionCorsMappings(registry);
    this.addDataAccessCorsMappings(registry);
    this.addDataManagementCorsMappings(registry);
  }

  protected void addStoreCorsMappings(CorsRegistry registry) {
    registry.addMapping("/store/about")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/categories")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/categories/*")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/front")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/product/*")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/checkout")
        .allowedOrigins("*")
        .allowedMethods("POST,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/store/checkout/validate")
        .allowedOrigins("*")
        .allowedMethods("POST,OPTIONS")
        .allowedHeaders("Authorization");
  }

  protected void addSessionCorsMappings(CorsRegistry registry) {
    registry.addMapping("/guest")
        .allowedOrigins("*")
        .allowedMethods("POST,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/login")
        .allowedOrigins("*")
        .allowedMethods("POST,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/profile")
        .allowedOrigins("*")
        .allowedMethods("GET,PUT,OPTIONS")
        .allowedHeaders("Authorization");

    registry.addMapping("/register")
        .allowedOrigins("*")
        .allowedMethods("POST,OPTIONS")
        .allowedHeaders("Authorization");
  }

  protected void addDataAccessCorsMappings(CorsRegistry registry) {
    registry.addMapping("/access/*")
        .allowedOrigins("*")
        .allowedMethods("GET,OPTIONS")
        .allowedHeaders("Authorization");
  }

  private void addDataManagementCorsMappings(CorsRegistry registry) {
    registry.addMapping("/data/**")
        .allowedOrigins("*")
        .allowedMethods("GET,POST,PUT,DELETE,OPTIONS")
        .allowedHeaders("Authorization");
  }
}
