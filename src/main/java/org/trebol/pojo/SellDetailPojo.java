package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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
  private int unitValue;
  @NotNull
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

  public int getUnitValue() {
    return unitValue;
  }

  public void setUnitValue(int unitValue) {
    this.unitValue = unitValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SellDetailPojo that = (SellDetailPojo) o;
    return units == that.units &&
        unitValue == that.unitValue &&
        Objects.equals(id, that.id) &&
        Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, units, product, unitValue);
  }

  @Override
  public String toString() {
    return "SellDetailPojo{" +
        "id=" + id +
        ", units=" + units +
        ", product=" + product +
        ", unitValue=" + unitValue +
        '}';
  }
}
