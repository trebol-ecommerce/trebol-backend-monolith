package cl.blm.trebol.store.jpa.entities;

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

import cl.blm.trebol.store.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "app_permissions")
@NamedQueries({ @NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p") })
public class Permission
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "permission_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 25)
  @Column(name = "permission_code")
  private String code;
  @Size(max = 100)
  @Column(name = "permission_description")
  private String description;

  public Permission() {
  }

  public Permission(Integer permissionId) {
    this.id = permissionId;
  }

  public Permission(Integer permissionId, String permissionCode) {
    this.id = permissionId;
    this.code = permissionCode;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
    if (!(object instanceof Permission)) {
      return false;
    }
    Permission other = (Permission) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.trebol.store.jpa.entities.Permission[ permissionId=" + id + " ]";
  }

}
