package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class UserRolePojo {
  @JsonInclude
  private Long id;
  @NotNull
  private String name;

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
    UserRolePojo that = (UserRolePojo) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "UserRolePojo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
