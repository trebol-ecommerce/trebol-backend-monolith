package org.trebol.jpa.entities;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

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
import javax.validation.constraints.Size;

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "sales")
@NamedQueries({ @NamedQuery(name = "Sell.findAll", query = "SELECT s FROM Sell s") })
public class Sell
    implements GenericEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "sell_id")
  private Long id;
  @Basic(optional = false)
  @Column(name = "sell_date")
  @Temporal(TemporalType.DATE)
  private Date date;
  @Basic(optional = false)
  @Column(name = "sell_total_value")
  private int totalValue;
  @Basic(optional = false)
  @Size(min = 1, max = 20)
  @Column(name = "session_extract")
  private String sessionExtract;
  @Size(min = 64, max = 64)
  @Column(name = "sell_token")
  private String token;
  @Basic(optional = false)
  @Column(name = "sell_total_items")
  private int totalItems;
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
  @ManyToOne(optional = false)
  private Customer customer;
  @JoinColumn(name = "salesperson_id", referencedColumnName = "salesperson_id")
  @ManyToOne(optional = true)
  private Salesperson salesperson;
  @JoinColumn(name = "sell_status_id", referencedColumnName = "sell_status_id")
  @ManyToOne(optional = false)
  private SellStatus status;
  @JoinColumn(name = "sell_type_id", referencedColumnName = "sell_type_id")
  @ManyToOne(optional = false)
  private SellType type;
  @JoinColumn(name = "sell_id", insertable = true, updatable = true, nullable = false)
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<SellDetail> details;

  public Sell() {
  }

  public Sell(Long id, Date date, int totalValue, String sessionExtract, String token, int totalItems,
      Customer customer, Salesperson salesperson, SellStatus status, SellType type, Collection<SellDetail> details) {
    this.id = id;
    this.date = date;
    this.totalValue = totalValue;
    this.sessionExtract = sessionExtract;
    this.token = token;
    this.totalItems = totalItems;
    this.customer = customer;
    this.salesperson = salesperson;
    this.status = status;
    this.type = type;
    this.details = details;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    hash = 97 * hash + Objects.hashCode(this.date);
    hash = 97 * hash + this.totalValue;
    hash = 97 * hash + Objects.hashCode(this.sessionExtract);
    hash = 97 * hash + Objects.hashCode(this.token);
    hash = 97 * hash + this.totalItems;
    hash = 97 * hash + Objects.hashCode(this.customer);
    hash = 97 * hash + Objects.hashCode(this.salesperson);
    hash = 97 * hash + Objects.hashCode(this.status);
    hash = 97 * hash + Objects.hashCode(this.type);
    hash = 97 * hash + Objects.hashCode(this.details);
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
    final Sell other = (Sell)obj;
    if (this.totalValue != other.totalValue) {
      return false;
    }
    if (this.totalItems != other.totalItems) {
      return false;
    }
    if (!Objects.equals(this.sessionExtract, other.sessionExtract)) {
      return false;
    }
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    if (!Objects.equals(this.customer, other.customer)) {
      return false;
    }
    if (!Objects.equals(this.salesperson, other.salesperson)) {
      return false;
    }
    if (!Objects.equals(this.status, other.status)) {
      return false;
    }
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.details, other.details)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Sell{id=" + id +
        ", date=" + date +
        ", totalValue=" + totalValue +
        ", sessionExtract=" + sessionExtract +
        ", token=" + token +
        ", totalItems=" + totalItems +
        ", customer=" + customer +
        ", salesperson=" + salesperson +
        ", status=" + status +
        ", type=" + type +
        ", details=" + details + '}';
  }

}
