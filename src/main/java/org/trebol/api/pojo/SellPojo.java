package org.trebol.api.pojo;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private Date date;
  @JsonInclude
  @NotNull
  private int subtotal;
  @JsonInclude
  @NotNull
  private SellTypePojo sellType;
  @JsonInclude
  @NotNull
  private CustomerPojo customer;
  @JsonInclude
  @Nullable
  private SalespersonPojo salesperson;
  @JsonInclude(value = Include.NON_EMPTY)
  @NotEmpty
  @Valid
  private Collection<SellDetailPojo> sellDetails;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(int subtotal) {
    this.subtotal = subtotal;
  }

  public SellTypePojo getSellType() {
    return sellType;
  }

  public void setSellType(SellTypePojo sellType) {
    this.sellType = sellType;
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

  public Collection<SellDetailPojo> getSellDetails() {
    return sellDetails;
  }

  public void setSellDetails(Collection<SellDetailPojo> sellDetails) {
    this.sellDetails = sellDetails;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    hash = 97 * hash + Objects.hashCode(this.date);
    hash = 97 * hash + this.subtotal;
    hash = 97 * hash + Objects.hashCode(this.sellType);
    hash = 97 * hash + Objects.hashCode(this.customer);
    hash = 97 * hash + Objects.hashCode(this.salesperson);
    hash = 97 * hash + Objects.hashCode(this.sellDetails);
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
    if (this.subtotal != other.subtotal) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.sellType, other.sellType)) {
      return false;
    }
    if (!Objects.equals(this.customer, other.customer)) {
      return false;
    }
    if (!Objects.equals(this.salesperson, other.salesperson)) {
      return false;
    }
    if (!Objects.equals(this.sellDetails, other.sellDetails)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "SellPojo{" + "id=" + id + ", date=" + date + ", subtotal=" + subtotal + ", sellType=" + sellType + ", customer=" + customer + ", salesperson=" + salesperson + ", sellDetails=" + sellDetails + '}';
  }

}
