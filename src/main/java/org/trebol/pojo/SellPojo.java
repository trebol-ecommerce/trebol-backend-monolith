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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@JsonInclude
public class SellPojo {
  private Long buyOrder;
  @JsonIgnore
  private String token;
  @JsonFormat(shape = STRING, pattern = "yyyy/MM/dd HH:mm:ss OOOO", timezone = "UTC")
  private Instant date;
  @Valid
  @NotEmpty
  @JsonInclude(NON_EMPTY)
  private Collection<SellDetailPojo> details;
  private int netValue;
  private int taxValue;
  private int transportValue;
  private int totalValue;
  private int totalItems;
  private String status;
  private String billingType;
  @NotBlank
  private String paymentType;
  @Valid
  private CustomerPojo customer;
  private SalespersonPojo salesperson;
  private ShipperPojo shipper;
  private BillingCompanyPojo billingCompany;
  private AddressPojo billingAddress;
  private AddressPojo shippingAddress;

  public SellPojo() { }

  public SellPojo(Long buyOrder, String token, Instant date, Collection<SellDetailPojo> details, int netValue,
                  int taxValue, int transportValue, int totalValue, int totalItems, String status, String billingType,
                  String paymentType, CustomerPojo customer, SalespersonPojo salesperson, ShipperPojo shipper,
                  BillingCompanyPojo billingCompany, AddressPojo billingAddress, AddressPojo shippingAddress) {
    this.buyOrder = buyOrder;
    this.token = token;
    this.date = date;
    this.details = details;
    this.netValue = netValue;
    this.taxValue = taxValue;
    this.transportValue = transportValue;
    this.totalValue = totalValue;
    this.totalItems = totalItems;
    this.status = status;
    this.billingType = billingType;
    this.paymentType = paymentType;
    this.customer = customer;
    this.salesperson = salesperson;
    this.shipper = shipper;
    this.billingCompany = billingCompany;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
  }

  public SellPojo(SellPojo source) {
    this.buyOrder = source.buyOrder;
    this.token = source.token;
    this.date = source.date;
    this.details = source.details;
    this.netValue = source.netValue;
    this.taxValue = source.taxValue;
    this.transportValue = source.transportValue;
    this.totalValue = source.totalValue;
    this.totalItems = source.totalItems;
    this.status = source.status;
    this.billingType = source.billingType;
    this.paymentType = source.paymentType;
    this.customer = source.customer;
    this.salesperson = source.salesperson;
    this.shipper = source.shipper;
    this.billingCompany = source.billingCompany;
    this.billingAddress = source.billingAddress;
    this.shippingAddress = source.shippingAddress;
  }

  public SellPojo(Long buyOrder) {
    this.buyOrder = buyOrder;
  }

  public SellPojo(Collection<SellDetailPojo> details, String billingType, String paymentType, CustomerPojo customer) {
    this.details = details;
    this.billingType = billingType;
    this.paymentType = paymentType;
    this.customer = customer;
  }
}
