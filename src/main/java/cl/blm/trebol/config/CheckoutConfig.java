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
  private String originURL;
  private String resourceURI;
  private String transactionValidationResourceURI;
  private String successPageURL;
  private String failurePageURL;
  private String transactionTokenPostDataKey;

  public CheckoutConfig() {
  }

  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public String getOriginURL() {
    return originURL;
  }

  public void setOriginURL(String originURL) {
    this.originURL = originURL;
  }

  public String getResourceURI() {
    return resourceURI;
  }

  public void setResourceURI(String resourceURI) {
    this.resourceURI = resourceURI;
  }

  public String getTransactionValidationResourceURI() {
    return transactionValidationResourceURI;
  }

  public void setTransactionValidationResourceURI(String transactionValidationResourceURI) {
    this.transactionValidationResourceURI = transactionValidationResourceURI;
  }

  public String getSuccessPageURL() {
    return successPageURL;
  }

  public void setSuccessPageURL(String successPageURL) {
    this.successPageURL = successPageURL;
  }

  public String getFailurePageURL() {
    return failurePageURL;
  }

  public void setFailurePageURL(String failurePageURL) {
    this.failurePageURL = failurePageURL;
  }

  public String getTransactionTokenPostDataKey() {
    return transactionTokenPostDataKey;
  }

  public void setTransactionTokenPostDataKey(String transactionTokenPostDataKey) {
    this.transactionTokenPostDataKey = transactionTokenPostDataKey;
  }
}
