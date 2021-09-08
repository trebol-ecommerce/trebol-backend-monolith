package org.trebol.api.pojo;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_NULL)
public class SellPojo {
  private Long id;
  @JsonIgnore
  private String token;
  @JsonFormat(shape = STRING, pattern = "yyyy/MM/dd HH:mm:ss OOOO", timezone = "UTC")
  private Instant date;
  @Valid
  @NotEmpty
  @JsonInclude(NON_EMPTY)
  private Collection<SellDetailPojo> details;
  @JsonInclude
  private int netValue;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Instant getDate() {
    return date;
  }

  public void setDate(Instant date) {
    this.date = date;
  }

  public Collection<SellDetailPojo> getDetails() {
    return details;
  }

  public void setDetails(Collection<SellDetailPojo> details) {
    this.details = details;
  }

  public int getNetValue() {
    return netValue;
  }

  public void setNetValue(int netValue) {
    this.netValue = netValue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getBillingType() {
    return billingType;
  }

  public void setBillingType(String billingType) {
    this.billingType = billingType;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public CustomerPojo getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerPojo customer) {
    this.customer = customer;
  }

  public SalespersonPojo getSalesperson() {
    return salesperson;
  }

  public void setSalesperson(SalespersonPojo salesperson) {
    this.salesperson = salesperson;
  }

  public ShipperPojo getShipper() {
    return shipper;
  }

  public void setShipper(ShipperPojo shipper) {
    this.shipper = shipper;
  }

  public BillingCompanyPojo getBillingCompany() {
    return billingCompany;
  }

  public void setBillingCompany(BillingCompanyPojo billingCompany) {
    this.billingCompany = billingCompany;
  }

  public AddressPojo getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(AddressPojo billingAddress) {
    this.billingAddress = billingAddress;
  }

  public AddressPojo getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(AddressPojo shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + Objects.hashCode(this.id);
    hash = 71 * hash + Objects.hashCode(this.token);
    hash = 71 * hash + Objects.hashCode(this.date);
    hash = 71 * hash + Objects.hashCode(this.details);
    hash = 71 * hash + this.netValue;
    hash = 71 * hash + Objects.hashCode(this.status);
    hash = 71 * hash + Objects.hashCode(this.billingType);
    hash = 71 * hash + Objects.hashCode(this.paymentType);
    hash = 71 * hash + Objects.hashCode(this.customer);
    hash = 71 * hash + Objects.hashCode(this.salesperson);
    hash = 71 * hash + Objects.hashCode(this.shipper);
    hash = 71 * hash + Objects.hashCode(this.billingCompany);
    hash = 71 * hash + Objects.hashCode(this.billingAddress);
    hash = 71 * hash + Objects.hashCode(this.shippingAddress);
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
    final SellPojo other = (SellPojo)obj;
    if (this.netValue != other.netValue) {
      return false;
    }
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    if (!Objects.equals(this.status, other.status)) {
      return false;
    }
    if (!Objects.equals(this.billingType, other.billingType)) {
      return false;
    }
    if (!Objects.equals(this.paymentType, other.paymentType)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.details, other.details)) {
      return false;
    }
    if (!Objects.equals(this.customer, other.customer)) {
      return false;
    }
    if (!Objects.equals(this.salesperson, other.salesperson)) {
      return false;
    }
    if (!Objects.equals(this.shipper, other.shipper)) {
      return false;
    }
    if (!Objects.equals(this.billingCompany, other.billingCompany)) {
      return false;
    }
    if (!Objects.equals(this.billingAddress, other.billingAddress)) {
      return false;
    }
    if (!Objects.equals(this.shippingAddress, other.shippingAddress)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "SellPojo{id=" + id +
        ", token=" + token +
        ", date=" + date +
        ", details=" + details +
        ", netValue=" + netValue +
        ", status=" + status +
        ", billingType=" + billingType +
        ", paymentType=" + paymentType +
        ", customer=" + customer +
        ", salesperson=" + salesperson +
        ", shipper=" + shipper +
        ", billingCompany=" + billingCompany +
        ", billingAddress=" + billingAddress +
        ", shippingAddress=" + shippingAddress + '}';
  }

}
