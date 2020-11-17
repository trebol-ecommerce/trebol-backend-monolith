package cl.blm.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Read-only wrapper class for settings read from 'custom.properties' file
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
@ConfigurationProperties(prefix = "external.checkout")
public class CheckoutConfig {
  private String serverURL;

  public CheckoutConfig() {
  }

  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }
}
