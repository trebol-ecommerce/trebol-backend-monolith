package org.trebol.api.pojo;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO received from the webpay checkout server after validating status of a transaction
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class WebpayValidationResponsePojo {
  @NotEmpty
  private Integer responseCode;
  @NotEmpty
  @DateTimeFormat(pattern = "MMddHHmm")
  private Date transactionDate;
  @NotEmpty
  private String sessionId;
  @NotEmpty
  private String buyOrder;

  public WebpayValidationResponsePojo() {
  }

  public Integer getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(Integer responseCode) {
    this.responseCode = responseCode;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getBuyOrder() {
    return buyOrder;
  }

  public void setBuyOrder(String buyOrder) {
    this.buyOrder = buyOrder;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.responseCode);
    hash = 97 * hash + Objects.hashCode(this.transactionDate);
    hash = 97 * hash + Objects.hashCode(this.sessionId);
    hash = 97 * hash + Objects.hashCode(this.buyOrder);
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
    final WebpayValidationResponsePojo other = (WebpayValidationResponsePojo)obj;
    if (!Objects.equals(this.sessionId, other.sessionId)) {
      return false;
    }
    if (!Objects.equals(this.buyOrder, other.buyOrder)) {
      return false;
    }
    if (!Objects.equals(this.responseCode, other.responseCode)) {
      return false;
    }
    if (!Objects.equals(this.transactionDate, other.transactionDate)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WebpayValidationResponsePojo{" + "responseCode=" + responseCode + ", transactionDate=" + transactionDate + ", sessionId=" + sessionId + ", buyOrder=" + buyOrder + '}';
  }
}
