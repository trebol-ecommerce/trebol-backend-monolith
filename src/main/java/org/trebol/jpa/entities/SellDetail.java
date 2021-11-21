package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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

  public SellDetail(SellDetail source) {
    this.id = source.id;
    this.units = source.units;
    this.product = source.product;
  }

  public SellDetail() { }

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
    return units == that.units && Objects.equals(id, that.id) && Objects.equals(unitValue, that.unitValue) && Objects.equals(product, that.product);
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
