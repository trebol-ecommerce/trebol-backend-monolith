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
@NamedQueries({ @NamedQuery(name = "SellDetail.findAll", query = "SELECT s FROM SellDetail s") })
public class SellDetail
  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "sell_detail_id")
  private Long id;
  @Basic(optional = false)
  @Column(name = "sell_detail_units")
  private int units;
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
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

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + Objects.hashCode(this.id);
    hash = 59 * hash + this.units;
    hash = 59 * hash + Objects.hashCode(this.product);
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
    final SellDetail other = (SellDetail)obj;
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
    return "SellDetail{id=" + id +
        ", units=" + units +
        ", product=" + product + '}';
  }
}
