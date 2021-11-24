package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(
  name = "app_users_roles",
  indexes = {
    @Index(columnList = "user_role_name")
  })
public class UserRole
  implements Serializable {

  private static final long serialVersionUID = 20L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_role_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 50)
  @Column(name = "user_role_name", nullable = false, unique = true)
  private String name;

  public UserRole(UserRole source) {
    this.id = source.id;
    this.name = source.name;
  }

  public UserRole() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserRole userRole = (UserRole) o;
    return Objects.equals(id, userRole.id) &&
        Objects.equals(name, userRole.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "UserRole{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
