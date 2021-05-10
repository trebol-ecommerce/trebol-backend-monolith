package org.trebol.api.pojo;

import java.util.Date;

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
}
