/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

;
@JsonInclude
public class SellDetailPojo {
  @JsonIgnore
  private Long id;
  @Min(1)
  private int units;
  private int unitValue;
  @NotNull
  private ProductPojo product;

  public SellDetailPojo() { }

  public SellDetailPojo(int units, ProductPojo product) {
    this.units = units;
    this.product = product;
  }

  public SellDetailPojo(Long id, int units, int unitValue, ProductPojo product) {
    this.id = id;
    this.units = units;
    this.unitValue = unitValue;
    this.product = product;
  }

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
