package cl.blm.trebol.jpa.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import cl.blm.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "sales")
@NamedQueries({ @NamedQuery(name = "Sell.findAll", query = "SELECT s FROM Sell s") })
public class Sell
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "sell_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Column(name = "sell_date")
  @Temporal(TemporalType.DATE)
  private Date date;
  @Basic(optional = false)
  @NotNull
  @Column(name = "sell_subtotal")
  private int subtotal;
  @JoinColumn(name = "sell_type_id", referencedColumnName = "sell_type_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private SellType sellType;
  @JoinColumn(name = "client_id", referencedColumnName = "client_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Client client;
  @JoinColumn(name = "seller_id", referencedColumnName = "seller_id", insertable = true, updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Seller seller;
  @JoinColumn(name = "sell_id", insertable = true, updatable = true, nullable = false)
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<SellDetail> sellDetails;

  public Sell() {
  }

  public Sell(Integer sellId) {
    this.id = sellId;
  }

  public Sell(Integer sellId, Date sellDate, int sellSubtotal) {
    this.id = sellId;
    this.date = sellDate;
    this.subtotal = sellSubtotal;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(int subtotal) {
    this.subtotal = subtotal;
  }

  public SellType getSellType() {
    return sellType;
  }

  public void setSellType(SellType sellType) {
    this.sellType = sellType;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  public Collection<SellDetail> getSellDetails() {
    return sellDetails;
  }

  public void setSellDetails(Collection<SellDetail> sellDetails) {
    this.sellDetails = sellDetails;
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
    if (!(object instanceof Sell)) {
      return false;
    }
    Sell other = (Sell) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.store.model.entities.Sell[ sellId=" + id + " ]";
  }

}
