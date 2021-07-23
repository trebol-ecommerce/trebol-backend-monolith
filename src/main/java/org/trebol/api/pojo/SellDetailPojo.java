package org.trebol.api.pojo;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellDetailPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private int units;
  @JsonInclude
  @NotNull
  private ProductPojo product;
  @JsonInclude(value = Include.NON_EMPTY)
  @Nullable
  private SellPojo sell;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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

  public SellPojo getSell() {
    return sell;
  }

  public void setSell(SellPojo sell) {
    this.sell = sell;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 29 * hash + Objects.hashCode(this.id);
    hash = 29 * hash + this.units;
    hash = 29 * hash + Objects.hashCode(this.product);
    hash = 29 * hash + Objects.hashCode(this.sell);
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
    if (!Objects.equals(this.product, other.product)) {
      return false;
    }
    if (!Objects.equals(this.sell, other.sell)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "SellDetailPojo{" + "id=" + id + ", units=" + units + ", product=" + product + ", sell=" + sell + '}';
  }

}
