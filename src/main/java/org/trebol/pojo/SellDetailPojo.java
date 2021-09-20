package org.trebol.pojo;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class SellDetailPojo {
  @JsonIgnore
  private Long id;
  @Min(1)
  private int units;
  @NotNull
  @Valid
  private ProductPojo product;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
  }

  public ProductPojo getProduct() {
    return product;
  }

  public void setProduct(ProductPojo product) {
    this.product = product;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 29 * hash + Objects.hashCode(this.id);
    hash = 29 * hash + this.units;
    hash = 29 * hash + Objects.hashCode(this.product);
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
    final SellDetailPojo other = (SellDetailPojo)obj;
    if (this.units != other.units) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.product, other.product);
  }

  @Override
  public String toString() {
    return "SellDetailPojo{" + "id=" + id + ", units=" + units + ", product=" + product + '}';
  }

}
