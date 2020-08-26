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
  private Integer sellDetailId;
  @JoinColumn(name = "sell_id", referencedColumnName = "sell_id")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Sell sell;
  @Basic(optional = false)
  @NotNull
  @Column(name = "sell_detail_units")
  private int sellDetailUnits;
  @JoinColumn(name = "product_id", referencedColumnName = "product_id")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Product product;

  public SellDetail() {
  }

  public SellDetail(Integer sellDetailId) {
    this.sellDetailId = sellDetailId;
  }

  public SellDetail(Integer sellDetailId, int sellDetailUnits) {
    this.sellDetailId = sellDetailId;
    this.sellDetailUnits = sellDetailUnits;
  }

  public Integer getSellDetailId() {
    return sellDetailId;
  }

  public void setSellDetailId(Integer sellDetailId) {
    this.sellDetailId = sellDetailId;
  }

  public int getSellDetailUnits() {
    return sellDetailUnits;
  }

  public void setSellDetailUnits(int sellDetailUnits) {
    this.sellDetailUnits = sellDetailUnits;
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
    hash += (sellDetailId != null ? sellDetailId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof SellDetail)) {
      return false;
    }
    SellDetail other = (SellDetail) object;
    if ((this.sellDetailId == null && other.sellDetailId != null)
        || (this.sellDetailId != null && !this.sellDetailId.equals(other.sellDetailId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.backend.model.entities.SellDetail[ sellDetailId=" + sellDetailId + " ]";
  }
}
