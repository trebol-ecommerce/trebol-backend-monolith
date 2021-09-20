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
@Table(name = "app_users_roles_permissions")
@NamedQueries({ @NamedQuery(name = "UserRolePermission.findAll", query = "SELECT u FROM UserRolePermission u") })
public class UserRolePermission
  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "user_role_permission_id")
  private Long id;
  @JoinColumn(name = "permission_id", referencedColumnName = "permission_id", updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Permission permission;
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id", updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserRole userRole;

  public UserRolePermission() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    int hash = 5;
    hash = 31 * hash + Objects.hashCode(this.id);
    hash = 31 * hash + Objects.hashCode(this.permission);
    hash = 31 * hash + Objects.hashCode(this.userRole);
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
    final UserRolePermission other = (UserRolePermission)obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.permission, other.permission)) {
      return false;
    }
    return Objects.equals(this.userRole, other.userRole);
  }

  @Override
  public String toString() {
    return "UserRolePermission{id=" + id +
        ", permission=" + permission +
        ", userRole=" + userRole + '}';
  }

}
