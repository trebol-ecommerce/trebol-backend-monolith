package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
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

  public SellPojo(Long buyOrder) {
    this.buyOrder = buyOrder;
  }

  public Long getBuyOrder() {
    return buyOrder;
  }

  public void setBuyOrder(Long buyOrder) {
    this.buyOrder = buyOrder;
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

  public int getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(int totalValue) {
    this.totalValue = totalValue;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SellPojo sellPojo = (SellPojo) o;
    return netValue == sellPojo.netValue &&
        taxValue == sellPojo.taxValue &&
        transportValue == sellPojo.transportValue &&
        totalValue == sellPojo.totalValue &&
        totalItems == sellPojo.totalItems &&
        Objects.equals(buyOrder, sellPojo.buyOrder) &&
        Objects.equals(token, sellPojo.token) &&
        Objects.equals(date, sellPojo.date) &&
        Objects.equals(details, sellPojo.details) &&
        Objects.equals(status, sellPojo.status) &&
        Objects.equals(billingType, sellPojo.billingType) &&
        Objects.equals(paymentType, sellPojo.paymentType) &&
        Objects.equals(customer, sellPojo.customer) &&
        Objects.equals(salesperson, sellPojo.salesperson) &&
        Objects.equals(shipper, sellPojo.shipper) &&
        Objects.equals(billingCompany, sellPojo.billingCompany) &&
        Objects.equals(billingAddress, sellPojo.billingAddress) &&
        Objects.equals(shippingAddress, sellPojo.shippingAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buyOrder, token, date, details, netValue, taxValue, transportValue, totalValue, totalItems,
        status, billingType, paymentType, customer, salesperson, shipper, billingCompany, billingAddress,
        shippingAddress);
  }

  @Override
  public String toString() {
    return "SellPojo{" +
        "buyOrder=" + buyOrder +
        ", token='" + token + '\'' +
        ", date=" + date +
        ", details=" + details +
        ", netValue=" + netValue +
        ", taxValue=" + taxValue +
        ", transportValue=" + transportValue +
        ", totalValue=" + totalValue +
        ", totalItems=" + totalItems +
        ", status='" + status + '\'' +
        ", billingType='" + billingType + '\'' +
        ", paymentType='" + paymentType + '\'' +
        ", customer=" + customer +
        ", salesperson=" + salesperson +
        ", shipper=" + shipper +
        ", billingCompany=" + billingCompany +
        ", billingAddress=" + billingAddress +
        ", shippingAddress=" + shippingAddress +
        '}';
  }
}
