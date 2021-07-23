package org.trebol.api.pojo;

import java.util.Objects;

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

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.token);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WebpayValidationRequestPojo other = (WebpayValidationRequestPojo)obj;
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WebpayValidationRequestPojo{" + "token=" + token + '}';
  }
}
