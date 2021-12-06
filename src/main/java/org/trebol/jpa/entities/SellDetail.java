package org.trebol.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "sell_details")
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
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false)
  @ManyToOne(optional = false)
  private Product product;

  public SellDetail() { }

  public SellDetail(SellDetail source) {
    this.id = source.id;
    this.units = source.units;
    this.unitValue = source.unitValue;
    this.product = source.product;
  }

  public SellDetail(int units, Product product) {
    this.units = units;
    this.product = product;
  }

  public SellDetail(Long id, int units, Integer unitValue, Product product) {
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

  public Integer getUnitValue() {
    return unitValue;
  }

  public void setUnitValue(Integer unitValue) {
    this.unitValue = unitValue;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SellDetail that = (SellDetail) o;
    return units == that.units &&
        Objects.equals(id, that.id) &&
        Objects.equals(unitValue, that.unitValue) &&
        Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, units, unitValue, product);
  }

  @Override
  public String toString() {
    return "SellDetail{" +
        "id=" + id +
        ", units=" + units +
        ", unitValue=" + unitValue +
        ", product=" + product +
        '}';
  }
}
