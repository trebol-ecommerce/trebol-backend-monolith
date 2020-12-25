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
import javax.validation.constraints.Size;

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
  @NotNull @Column(name = "sell_total_value")
  private int totalValue;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "session_extract")
  private String sessionExtract;
  @Basic(optional = true)
  @Size(min = 64, max = 64)
  @Column(name = "sell_token")
  private String token;
  @Basic(optional = false)
  @NotNull
  @Column(name = "sell_total_items")
  private int totalItems;
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
  @ManyToOne(optional = false)
  private Customer customer;
  @JoinColumn(name = "salesperson_id", referencedColumnName = "salesperson_id")
  @ManyToOne
  private Salesperson salesperson;
  @JoinColumn(name = "sell_status_id", referencedColumnName = "sell_status_id")
  @ManyToOne
  private SellStatus status;
  @JoinColumn(name = "sell_type_id", referencedColumnName = "sell_type_id")
  @ManyToOne(optional = false)
  private SellType type;
  @JoinColumn(name = "sell_id", insertable = true, updatable = true, nullable = false)
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<SellDetail> details;

  public Sell() {
  }

  public Sell(Integer sellId) {
    this.id = sellId;
  }

  public Sell(Integer sellId, Date sellDate, int sellTotalValue, String sessionExtract, int sellTotalItems) {
    this.id = sellId;
    this.date = sellDate;
    this.totalValue = sellTotalValue;
    this.sessionExtract = sessionExtract;
    this.totalItems = sellTotalItems;
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

  public int getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(int totalValue) {
    this.totalValue = totalValue;
  }

  public String getSessionExtract() {
    return sessionExtract;
  }

  public void setSessionExtract(String sessionExtract) {
    this.sessionExtract = sessionExtract;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Salesperson getSalesperson() {
    return salesperson;
  }

  public void setSalesperson(Salesperson salesperson) {
    this.salesperson = salesperson;
  }

  public SellStatus getStatus() {
    return status;
  }

  public void setStatus(SellStatus status) {
    this.status = status;
  }

  public SellType getType() {
    return type;
  }

  public void setType(SellType type) {
    this.type = type;
  }

  public Collection<SellDetail> getDetails() {
    return details;
  }

  public void setDetails(Collection<SellDetail> details) {
    this.details = details;
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
    Sell other = (Sell)object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.trebol.jpa.entities.Sell[ id=" + id + " ]";
  }

}
