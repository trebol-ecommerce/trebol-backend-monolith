package cl.blm.trebol.api.pojo;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude
public class WebpayCheckoutResponsePojo {
  @NotEmpty
  private String url;
  @NotEmpty
  private String token_ws;

  public WebpayCheckoutResponsePojo() {
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getToken_ws() {
    return token_ws;
  }

  public void setToken_ws(String token_ws) {
    this.token_ws = token_ws;
  }
}
