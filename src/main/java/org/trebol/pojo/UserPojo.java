package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_NULL)
public class UserPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  @JsonInclude
  private String name;
  @NotBlank
  private String password;
  private PersonPojo person;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserPojo userPojo = (UserPojo) o;
    return Objects.equals(id, userPojo.id) &&
        Objects.equals(name, userPojo.name) &&
        Objects.equals(password, userPojo.password) &&
        Objects.equals(person, userPojo.person) &&
        Objects.equals(role, userPojo.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, password, person, role);
  }

  @Override
  public String toString() {
    return "UserPojo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", person=" + person +
        ", role='" + role + '\'' +
        '}';
  }
}
