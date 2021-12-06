package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

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

  public UserRole() { }

  public UserRole(UserRole source) {
    this.id = source.id;
    this.name = source.name;
  }

  public UserRole(Long id, String name) {
    this.id = id;
    this.name = name;
  }

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
