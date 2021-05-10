package org.trebol.api.pojo;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ReceiptPojo {
  private int buyOrder;
  private int amount;
  private Collection<ReceiptDetailPojo> details;
  private String date;
  private String status;

  public int getBuyOrder() {
    return buyOrder;
  }

  public void setBuyOrder(int buyOrder) {
    this.buyOrder = buyOrder;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public Collection<ReceiptDetailPojo> getDetails() {
    return details;
  }

  public void setDetails(Collection<ReceiptDetailPojo> details) {
    this.details = details;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
