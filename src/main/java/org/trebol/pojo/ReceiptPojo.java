/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

@JsonInclude
public class ReceiptPojo {
  private long buyOrder;
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
    return Objects.hash(buyOrder, details, date, status, token, totalValue, taxValue, transportValue, totalItems);
  }

  @Override
  public String toString() {
    return "ReceiptPojo{" +
        "buyOrder=" + buyOrder +
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
