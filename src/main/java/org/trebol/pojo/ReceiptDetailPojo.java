package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ReceiptDetailPojo {
  private ProductPojo product;
  private int units;
  private Integer unitValue;

  public ProductPojo getProduct() {
    return product;
  }

  public void setProduct(ProductPojo product) {
    this.product = product;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
  }

  public Integer getUnitValue() {
    return unitValue;
  }

  public void setUnitValue(Integer unitValue) {
    this.unitValue = unitValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReceiptDetailPojo that = (ReceiptDetailPojo) o;
    return units == that.units &&
        Objects.equals(product, that.product) &&
        Objects.equals(unitValue, that.unitValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(product, units, unitValue);
  }

  @Override
  public String toString() {
    return "ReceiptDetailPojo{" +
        "product=" + product +
        ", units=" + units +
        ", unitValue=" + unitValue +
        '}';
  }
}
