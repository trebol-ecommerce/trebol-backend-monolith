package cl.blm.trebol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@SpringBootApplication
public class BackendApp {
  /**
   * Application starting point.
   *
   * @param args
   */
  public static void main(String[] args) {
    System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    SpringApplication.run(BackendApp.class, args);
  }

}
