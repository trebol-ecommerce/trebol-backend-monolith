package org.trebol.api.pojo;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO sent to the webpay checkout server to validate status of a succesful or failed transaction
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class WebpayValidationRequestPojo {
  @NotEmpty
  private String token;

  public WebpayValidationRequestPojo() {
  }

  public WebpayValidationRequestPojo(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
