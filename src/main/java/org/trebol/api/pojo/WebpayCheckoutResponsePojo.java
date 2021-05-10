package org.trebol.api.pojo;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO included in the response body by the checkout server after a succesful transaction
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude
public class WebpayCheckoutResponsePojo {
  @NotEmpty
  private String url;
  @NotEmpty
  private String token;

  public WebpayCheckoutResponsePojo() {
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
