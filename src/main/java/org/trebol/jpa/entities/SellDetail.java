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

package org.trebol.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sell_details")
@NoArgsConstructor
@Getter
@Setter
public class SellDetail
  implements Serializable {
  private static final long serialVersionUID = 15L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sell_detail_id", nullable = false)
  private Long id;
  @Column(name = "sell_detail_units", nullable = false)
  private int units;
  @Column(name = "sell_detail_unit_value", nullable = false)
  private Integer unitValue;
  @Column(name = "sell_detail_description", nullable = false)
  @Size(max = 260)
  private String description;
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false,
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @ManyToOne(optional = false)
  private Product product;
  @JoinColumn(name = "sell_id", referencedColumnName = "sell_id", insertable = false, updatable = false,
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Sell sell;

  public SellDetail(SellDetail source) {
    this.id = source.id;
    this.units = source.units;
    this.unitValue = source.unitValue;
    this.description = source.description;
    this.product = source.product;
    this.sell = source.sell;
  }

  public SellDetail(int units, Product product) {
    this.units = units;
    this.product = product;
    this.unitValue = product.getPrice();
    this.description = String.format("%o x %s [%s] | Unit Val: %d",
      units, product.getName(), product.getBarcode(), product.getPrice());
  }

  public SellDetail(Long id, int units, Integer unitValue, Product product) {
    this.id = id;
    this.units = units;
    this.unitValue = unitValue;
    this.product = product;
    this.description = String.format("%o x %s [%s] | Unit Val: %d",
      units, product.getName(), product.getBarcode(), product.getPrice());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SellDetail that = (SellDetail) o;
    return units == that.units &&
      Objects.equals(id, that.id) &&
      Objects.equals(unitValue, that.unitValue) &&
      Objects.equals(product, that.product) &&
      Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, units, unitValue, product, description);
  }

  @Override
  public String toString() {
    return "SellDetail{" +
      "id=" + id +
      ", units=" + units +
      ", unitValue=" + unitValue +
      ", product=" + product +
      ", description=" + description +
      '}';
  }
}
