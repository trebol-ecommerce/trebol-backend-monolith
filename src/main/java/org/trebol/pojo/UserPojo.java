package org.trebol.pojo;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class UserPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String password;
  @JsonInclude(NON_NULL)
  private PersonPojo person;
  @JsonInclude(NON_NULL)
  private String role;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public PersonPojo getPerson() {
    return person;
  }

  public void setPerson(PersonPojo person) {
    this.person = person;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.id);
    hash = 37 * hash + Objects.hashCode(this.name);
    hash = 37 * hash + Objects.hashCode(this.password);
    hash = 37 * hash + Objects.hashCode(this.person);
    hash = 37 * hash + Objects.hashCode(this.role);
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
    final UserPojo other = (UserPojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    if (!Objects.equals(this.role, other.role)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.person, other.person);
  }

  @Override
  public String toString() {
    return "UserPojo{" + "id=" + id + ", name=" + name + ", password=" + password + ", person=" + person + ", role=" + role + '}';
  }
}
