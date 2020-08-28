/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.blm.newmarketing.backend.model.entities;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;

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
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "sell_detail_units")
  private int units;
  @JoinColumn(name = "sell_id", referencedColumnName = "sell_id", insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Sell sell;
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Product product;

  public SellDetail() {
  }

  public SellDetail(Integer sellDetailId) {
    this.id = sellDetailId;
  }

  public SellDetail(Integer sellDetailId, int sellDetailUnits) {
    this.id = sellDetailId;
    this.units = sellDetailUnits;
  }

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

  public Sell getSell() {
    return sell;
  }

  public void setSell(Sell sell) {
    this.sell = sell;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof SellDetail)) {
      return false;
    }
    SellDetail other = (SellDetail) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.backend.model.entities.SellDetail[ sellDetailId=" + id + " ]";
  }
}
