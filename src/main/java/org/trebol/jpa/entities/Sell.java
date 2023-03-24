/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

package org.trebol.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(
  name = "sales",
  indexes = {
    @Index(columnList = "sell_date"),
    @Index(columnList = "sell_transaction_token"),
  })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Sell
  implements Serializable {
  private static final long serialVersionUID = 14L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sell_id", nullable = false)
  private Long id;
  @Column(name = "sell_date", nullable = false)
  @CreationTimestamp
  private Instant date;
  @Column(name = "sell_total_items", nullable = false)
  private int totalItems;
  @Column(name = "sell_net_value", nullable = false)
  private int netValue;
  @Column(name = "sell_transport_value", nullable = false)
  private int transportValue;
  @Column(name = "sell_taxes_value", nullable = false)
  private int taxesValue;
  @Column(name = "sell_total_value", nullable = false)
  private int totalValue;
  @Size(min = 64, max = 64)
  @Column(name = "sell_transaction_token")
  private String transactionToken;
  @JoinColumn(name = "customer_id")
  @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
  private Customer customer;
  @JoinColumn(name = "payment_type_id", updatable = false, nullable = false)
  @ManyToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  private PaymentType paymentType;
  @JoinColumn(name = "sell_status_id", updatable = false, nullable = false)
  @ManyToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  private SellStatus status;
  @JoinColumn(name = "billing_type_id", updatable = false, nullable = false)
  @ManyToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  private BillingType billingType;
  @JoinColumn(name = "billing_company_id")
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private BillingCompany billingCompany;
  @JoinColumn(name = "billing_address_id", updatable = false)
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Address billingAddress;
  @JoinColumn(name = "shipper_id", updatable = false)
  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  private Shipper shipper;
  @JoinColumn(name = "shipping_address_id", updatable = false)
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Address shippingAddress;
  @JoinColumn(name = "salesperson_id")
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Salesperson salesperson;
  @JoinColumn(name = "sell_id", nullable = false)
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<SellDetail> details;

  public Sell(Sell source) {
    this.id = source.id;
    this.date = Instant.from(source.date);
    this.totalItems = source.totalItems;
    this.netValue = source.netValue;
    this.transportValue = source.transportValue;
    this.taxesValue = source.taxesValue;
    this.totalValue = source.totalValue;
    this.transactionToken = source.transactionToken;
    this.paymentType = source.paymentType;
    this.status = source.status;
    this.billingType = source.billingType;
    this.billingAddress = new Address(source.billingAddress);
    this.customer = new Customer(source.customer);
    this.details = source.details.stream()
      .map(sourceDetail -> {
        SellDetail target = new SellDetail(sourceDetail);
        target.setSell(this);
        return target;
      })
      .collect(Collectors.toList());
    if (source.billingCompany != null) {
      this.billingCompany = new BillingCompany(source.billingCompany);
    }
    if (source.shipper != null) {
      this.shipper = new Shipper(source.shipper);
    }
    if (source.shippingAddress != null) {
      this.shippingAddress = new Address(source.shippingAddress);
    }
    if (source.salesperson != null) {
      this.salesperson = new Salesperson(source.salesperson);
    }
  }
}
