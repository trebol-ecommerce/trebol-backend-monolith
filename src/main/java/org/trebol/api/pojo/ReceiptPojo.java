package org.trebol.api.pojo;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 43 * hash + this.buyOrder;
    hash = 43 * hash + this.amount;
    hash = 43 * hash + Objects.hashCode(this.details);
    hash = 43 * hash + Objects.hashCode(this.date);
    hash = 43 * hash + Objects.hashCode(this.status);
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
    final ReceiptPojo other = (ReceiptPojo)obj;
    if (this.buyOrder != other.buyOrder) {
      return false;
    }
    if (this.amount != other.amount) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.status, other.status)) {
      return false;
    }
    if (!Objects.equals(this.details, other.details)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ReceiptPojo{" + "buyOrder=" + buyOrder + ", amount=" + amount + ", details=" + details + ", date=" + date + ", status=" + status + '}';
  }
}
