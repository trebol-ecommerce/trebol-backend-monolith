package org.trebol.api.pojo;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO sent to the webpay checkout server to request a new transaction
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude
public class WebpayCheckoutRequestPojo {
  private int amount;
  @NotEmpty
  private String sessionId;
  @NotEmpty
  private String transactionId;

  public WebpayCheckoutRequestPojo() {
  }

  public WebpayCheckoutRequestPojo(int amount, String sessionId, String transactionId) {
    this.amount = amount;
    this.sessionId = sessionId;
    this.transactionId = transactionId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

}
