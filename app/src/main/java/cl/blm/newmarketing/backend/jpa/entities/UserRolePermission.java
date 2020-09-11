package cl.blm.newmarketing.backend.jpa.entities;

import cl.blm.newmarketing.backend.jpa.GenericEntity;
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

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "user_role_permissions")
@NamedQueries({
  @NamedQuery(name = "UserRolePermission.findAll", query = "SELECT u FROM UserRolePermission u")})
public class UserRolePermission implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "user_role_permission_id")
  private Integer id;
  @JoinColumn(name = "permission_id", referencedColumnName = "permission_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Permission permission;
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserRole userRole;

  public UserRolePermission() {
  }

  public UserRolePermission(Integer userRolePermissionId) {
    this.id = userRolePermissionId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Permission getPermission() {
    return permission;
  }

  public void setPermission(Permission permission) {
    this.permission = permission;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
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
    if (!(object instanceof UserRolePermission)) {
      return false;
    }
    UserRolePermission other = (UserRolePermission) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.backend.jpa.entities.UserRolePermission[ userRolePermissionId=" + id + " ]";
  }
  
}
