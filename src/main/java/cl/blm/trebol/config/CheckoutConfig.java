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
  private Integer serverURL;

  public CheckoutConfig() {
  }

  public Integer getServerURL() {
    return serverURL;
  }

  public void setServerURL(Integer serverURL) {
    this.serverURL = serverURL;
  }
}
