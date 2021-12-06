package org.trebol.pojo;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ReceiptPojo {
  private long buyOrder;
  @Deprecated(forRemoval = true)
  private int amount;
  private Collection<ReceiptDetailPojo> details;
  private Instant date;
  private String status;
  private String token;
  private int totalValue;
  private int taxValue;
  private int transportValue;
  private int totalItems;

  public long getBuyOrder() {
    return buyOrder;
  }

  public void setBuyOrder(long buyOrder) {
    this.buyOrder = buyOrder;
  }

  @Deprecated(forRemoval = true, since = "1.1.0")
  public int getAmount() {
    return amount;
  }

  @Deprecated(forRemoval = true, since = "1.1.0")
  public void setAmount(int amount) {
    this.amount = amount;
  }

  public Collection<ReceiptDetailPojo> getDetails() {
    return details;
  }

  public void setDetails(Collection<ReceiptDetailPojo> details) {
    this.details = details;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(int totalValue) {
    this.totalValue = totalValue;
  }

  public int getTaxValue() {
    return taxValue;
  }

  public void setTaxValue(int taxValue) {
    this.taxValue = taxValue;
  }

  public int getTransportValue() {
    return transportValue;
  }

  public void setTransportValue(int transportValue) {
    this.transportValue = transportValue;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReceiptPojo that = (ReceiptPojo) o;
    return buyOrder == that.buyOrder &&
        amount == that.amount &&
        totalValue == that.totalValue &&
        taxValue == that.taxValue &&
        transportValue == that.transportValue &&
        totalItems == that.totalItems &&
        Objects.equals(details, that.details) &&
        Objects.equals(date, that.date) &&
        Objects.equals(status, that.status) &&
        Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buyOrder, amount, details, date, status, token, totalValue, taxValue, transportValue, totalItems);
  }

  @Override
  public String toString() {
    return "ReceiptPojo{" +
        "buyOrder=" + buyOrder +
        ", amount=" + amount +
        ", details=" + details +
        ", date=" + date +
        ", status='" + status + '\'' +
        ", token='" + token + '\'' +
        ", totalValue=" + totalValue +
        ", taxValue=" + taxValue +
        ", transportValue=" + transportValue +
        ", totalItems=" + totalItems +
        '}';
  }
}
