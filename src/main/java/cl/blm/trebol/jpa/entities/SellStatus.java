package cl.blm.trebol.jpa.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.blm.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "sell_statuses")
@NamedQueries({ @NamedQuery(name = "SellStatus.findAll", query = "SELECT s FROM SellStatus s") })
public class SellStatus
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "sell_status_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull @Size(min = 1, max = 100)
  @Column(name = "sell_status_name")
  private String name;

  public SellStatus() {
  }

  public SellStatus(Integer sellStatusId) {
    this.id = sellStatusId;
  }

  public SellStatus(Integer sellStatusId, String sellStatusName) {
    this.id = sellStatusId;
    this.name = sellStatusName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    if (!(object instanceof SellStatus)) {
      return false;
    }
    SellStatus other = (SellStatus)object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.trebol.jpa.entities.SellStatus[ id=" + id + " ]";
  }

}
