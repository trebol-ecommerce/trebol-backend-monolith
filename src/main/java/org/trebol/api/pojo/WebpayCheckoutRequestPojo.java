package org.trebol.api.pojo;

import java.util.Objects;

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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 17 * hash + this.amount;
    hash = 17 * hash + Objects.hashCode(this.sessionId);
    hash = 17 * hash + Objects.hashCode(this.transactionId);
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
    final WebpayCheckoutRequestPojo other = (WebpayCheckoutRequestPojo)obj;
    if (this.amount != other.amount) {
      return false;
    }
    if (!Objects.equals(this.sessionId, other.sessionId)) {
      return false;
    }
    if (!Objects.equals(this.transactionId, other.transactionId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WebpayCheckoutRequestPojo{" + "amount=" + amount + ", sessionId=" + sessionId + ", transactionId=" + transactionId + '}';
  }

}
