package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Wrapper class for data needed to redirect towards payment page
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class PaymentRedirectionDetailsPojo {
  private String url;
  private String token;

  public PaymentRedirectionDetailsPojo() {
  }

  public PaymentRedirectionDetailsPojo(
    String url,
    String token
  ) {
    this.url = url;
    this.token = token;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
