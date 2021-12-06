package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "app_permissions")
public class Permission
  implements Serializable {

  private static final long serialVersionUID = 8L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "permission_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 25)
  @Column(name = "permission_code", nullable = false, unique = true)
  private String code;
  @Size(max = 100)
  @Column(name = "permission_description")
  private String description;

  public Permission() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permission that = (Permission) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(code, that.code) &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, description);
  }

  @Override
  public String toString() {
    return "Permission{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
