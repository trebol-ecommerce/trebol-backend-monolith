package org.trebol.integration.payments.webpayplus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 *
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.integration.payment.webpayplus")
public class WebpayplusPaymentProperties {
  @NotNull
  private Boolean production;
  private String commerceCode;
  private String apiKey;
  @NotBlank
  private String callbackUrl;
  @NotBlank
  private String browserRedirectionUrl;

  public Boolean isProduction() {
    return production;
  }

  public void setProduction(Boolean production) {
    this.production = production;
  }

  public String getCommerceCode() {
    return commerceCode;
  }

  public void setCommerceCode(String commerceCode) {
    this.commerceCode = commerceCode;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getBrowserRedirectionUrl() {
    return browserRedirectionUrl;
  }

  public void setBrowserRedirectionUrl(String browserRedirectionUrl) {
    this.browserRedirectionUrl = browserRedirectionUrl;
  }

}
