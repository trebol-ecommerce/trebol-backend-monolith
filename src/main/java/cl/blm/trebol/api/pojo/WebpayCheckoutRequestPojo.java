package cl.blm.trebol.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude
public class WebpayCheckoutRequestPojo {
  private Integer tr_amount;
  private String tr_session;
  private String tr_id;

  public WebpayCheckoutRequestPojo() {
  }

  public WebpayCheckoutRequestPojo(Integer tr_amount, String tr_session, String tr_id) {
    this.tr_amount = tr_amount;
    this.tr_session = tr_session;
    this.tr_id = tr_id;
  }

  public Integer getTr_amount() {
    return tr_amount;
  }

  public void setTr_amount(Integer tr_amount) {
    this.tr_amount = tr_amount;
  }

  public String getTr_session() {
    return tr_session;
  }

  public void setTr_session(String tr_session) {
    this.tr_session = tr_session;
  }

  public String getTr_id() {
    return tr_id;
  }

  public void setTr_id(String tr_id) {
    this.tr_id = tr_id;
  }

}
