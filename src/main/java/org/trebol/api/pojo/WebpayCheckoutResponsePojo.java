package org.trebol.api.pojo;

import java.util.Objects;

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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.url);
    hash = 17 * hash + Objects.hashCode(this.token);
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
    final WebpayCheckoutResponsePojo other = (WebpayCheckoutResponsePojo)obj;
    if (!Objects.equals(this.url, other.url)) {
      return false;
    }
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WebpayCheckoutResponsePojo{" + "url=" + url + ", token=" + token + '}';
  }
}
