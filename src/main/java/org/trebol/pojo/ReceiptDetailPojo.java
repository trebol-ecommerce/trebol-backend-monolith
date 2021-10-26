package org.trebol.pojo;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ReceiptDetailPojo {
  private ProductPojo product;
  private int units;
  private int unitPrice=product.getPrice();

  public ProductPojo getProduct() {
    return product;
  }

  public void setProduct(ProductPojo product) {
    this.product = product;
  }

  public int getUnits() {
    return units;
  }

  public int getUnitPrice() { return unitPrice; }

  public void setUnits(int units) {
    this.units = units;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.product);
    hash = 11 * hash + this.units;
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
    final ReceiptDetailPojo other = (ReceiptDetailPojo)obj;
    if (this.units != other.units) {
      return false;
    }
    return Objects.equals(this.product, other.product);
  }

  @Override
  public String toString() {
    return "ReceiptDetailPojo{" + "product=" + product + ", units=" + units + ", unitPrice=" + unitPrice + + '}';
  }

}
