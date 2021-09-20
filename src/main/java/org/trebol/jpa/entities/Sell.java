package org.trebol.jpa.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "sales")
@NamedQueries({ @NamedQuery(name = "Sell.findAll", query = "SELECT s FROM Sell s") })
public class Sell
  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "sell_id")
  private Long id;
  @Basic(optional = false)
  @Column(name = "sell_date")
  @CreationTimestamp
  private Instant date;
  @Basic(optional = false)
  @Column(name = "sell_total_items")
  private int totalItems;
  @Basic(optional = false)
  @Column(name = "sell_net_value")
  private int netValue;
  @Basic(optional = false)
  @Column(name = "sell_transport_value")
  private int transportValue;
  @Basic(optional = false)
  @Column(name = "sell_taxes_value")
  private int taxesValue;
  @Basic(optional = false)
  @Column(name = "sell_total_value")
  private int totalValue;
  @Size(min = 64, max = 64)
  @Column(name = "sell_transaction_token")
  private String transactionToken;
  @JoinColumn(name = "customer_id")
  @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
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
  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private BillingCompany billingCompany;
  @JoinColumn(name = "billing_address_id", updatable = false)
  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private Address billingAddress;
  @JoinColumn(name = "shipper_id", updatable = false)
  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  private Shipper shipper;
  @JoinColumn(name = "shipping_address_id", updatable = false)
  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private Address shippingAddress;
  @JoinColumn(name = "salesperson_id")
  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private Salesperson salesperson;
  @JoinColumn(name = "sell_id", nullable = false)
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<SellDetail> details;

  public Sell() { }

  public Sell(Sell source) {
    this.id = source.id;
    this.date = source.date;
    this.totalItems = source.totalItems;
    this.netValue = source.netValue;
    this.transportValue = source.transportValue;
    this.taxesValue = source.taxesValue;
    this.totalValue = source.totalValue;
    this.transactionToken = source.transactionToken;
    this.customer = source.customer;
    this.paymentType = source.paymentType;
    this.status = source.status;
    this.billingType = source.billingType;
    this.billingCompany = source.billingCompany;
    this.billingAddress = source.billingAddress;
    this.shipper = source.shipper;
    this.shippingAddress = source.shippingAddress;
    this.salesperson = source.salesperson;
    this.details = source.details;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public int getNetValue() {
    return netValue;
  }

  public void setNetValue(int netValue) {
    this.netValue = netValue;
  }

  public int getTransportValue() {
    return transportValue;
  }

  public void setTransportValue(int transportValue) {
    this.transportValue = transportValue;
  }

  public int getTaxesValue() {
    return taxesValue;
  }

  public void setTaxesValue(int taxesValue) {
    this.taxesValue = taxesValue;
  }

  public int getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(int totalValue) {
    this.totalValue = totalValue;
  }

  public String getTransactionToken() {
    return transactionToken;
  }

  public void setTransactionToken(String transactionToken) {
    this.transactionToken = transactionToken;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public PaymentType getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(PaymentType paymentType) {
    this.paymentType = paymentType;
  }

  public SellStatus getStatus() {
    return status;
  }

  public void setStatus(SellStatus status) {
    this.status = status;
  }

  public BillingType getBillingType() {
    return billingType;
  }

  public void setBillingType(BillingType billingType) {
    this.billingType = billingType;
  }

  public BillingCompany getBillingCompany() {
    return billingCompany;
  }

  public void setBillingCompany(BillingCompany billingCompany) {
    this.billingCompany = billingCompany;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public Shipper getShipper() {
    return shipper;
  }

  public void setShipper(Shipper shipper) {
    this.shipper = shipper;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Salesperson getSalesperson() {
    return salesperson;
  }

  public void setSalesperson(Salesperson salesperson) {
    this.salesperson = salesperson;
  }

  public Collection<SellDetail> getDetails() {
    return details;
  }

  public void setDetails(Collection<SellDetail> details) {
    this.details = details;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.id);
    hash = 67 * hash + Objects.hashCode(this.date);
    hash = 67 * hash + this.totalItems;
    hash = 67 * hash + this.netValue;
    hash = 67 * hash + this.transportValue;
    hash = 67 * hash + this.taxesValue;
    hash = 67 * hash + this.totalValue;
    hash = 67 * hash + Objects.hashCode(this.transactionToken);
    hash = 67 * hash + Objects.hashCode(this.customer);
    hash = 67 * hash + Objects.hashCode(this.paymentType);
    hash = 67 * hash + Objects.hashCode(this.status);
    hash = 67 * hash + Objects.hashCode(this.billingType);
    hash = 67 * hash + Objects.hashCode(this.billingCompany);
    hash = 67 * hash + Objects.hashCode(this.billingAddress);
    hash = 67 * hash + Objects.hashCode(this.shipper);
    hash = 67 * hash + Objects.hashCode(this.shippingAddress);
    hash = 67 * hash + Objects.hashCode(this.salesperson);
    hash = 67 * hash + Objects.hashCode(this.details);
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
    final Sell other = (Sell)obj;
    if (this.totalItems != other.totalItems) {
      return false;
    }
    if (this.netValue != other.netValue) {
      return false;
    }
    if (this.transportValue != other.transportValue) {
      return false;
    }
    if (this.taxesValue != other.taxesValue) {
      return false;
    }
    if (this.totalValue != other.totalValue) {
      return false;
    }
    if (!Objects.equals(this.transactionToken, other.transactionToken)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.customer, other.customer)) {
      return false;
    }
    if (!Objects.equals(this.paymentType, other.paymentType)) {
      return false;
    }
    if (!Objects.equals(this.status, other.status)) {
      return false;
    }
    if (!Objects.equals(this.billingType, other.billingType)) {
      return false;
    }
    if (!Objects.equals(this.billingCompany, other.billingCompany)) {
      return false;
    }
    if (!Objects.equals(this.billingAddress, other.billingAddress)) {
      return false;
    }
    if (!Objects.equals(this.shipper, other.shipper)) {
      return false;
    }
    if (!Objects.equals(this.shippingAddress, other.shippingAddress)) {
      return false;
    }
    if (!Objects.equals(this.salesperson, other.salesperson)) {
      return false;
    }
    return Objects.equals(this.details, other.details);
  }

  @Override
  public String toString() {
    return "Sell{id=" + id +
        ", date=" + date +
        ", totalItems=" + totalItems +
        ", netValue=" + netValue +
        ", transportValue=" + transportValue +
        ", taxesValue=" + taxesValue +
        ", totalValue=" + totalValue +
        ", transactionToken=" + transactionToken +
        ", customer=" + customer +
        ", paymentType=" + paymentType +
        ", status=" + status +
        ", billingType=" + billingType +
        ", billingCompany=" + billingCompany +
        ", billingAddress=" + billingAddress +
        ", shipper=" + shipper +
        ", shippingAddress=" + shippingAddress +
        ", salesperson=" + salesperson +
        ", details=" + details + '}';
  }



}
