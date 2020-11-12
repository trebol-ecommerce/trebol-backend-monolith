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
@Table(name = "app_params")
@NamedQueries({
  @NamedQuery(name = "Param.findAll", query = "SELECT p FROM Param p")})
public class Param
    implements GenericEntity<Integer> {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "param_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 25)
  @Column(name = "param_category")
  private String category;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "param_name")
  private String name;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 500)
  @Column(name = "param_value")
  private String value;

  public Param() {
  }

  public Param(Integer paramId) {
    this.id = paramId;
  }

  public Param(Integer paramId, String paramCategory, String paramName, String paramValue) {
    this.id = paramId;
    this.category = paramCategory;
    this.name = paramName;
    this.value = paramValue;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
    if (!(object instanceof Param)) {
      return false;
    }
    Param other = (Param)object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.trebol.store.jpa.entities.Param[ paramId=" + id + " ]";
  }

}
