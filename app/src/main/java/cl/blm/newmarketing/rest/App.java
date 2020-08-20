package cl.blm.newmarketing.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@SpringBootApplication
@ComponentScan
@PropertySources({@PropertySource("classpath:logging.properties")})
public class App {
  /**
   * Application starting point.
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}
