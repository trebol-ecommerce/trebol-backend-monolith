package org.trebol.pojo;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_NULL)
public class PersonPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String idNumber;
  @NotBlank
  private String email;
  @Pattern(regexp = "^(((\\(\\+?[0-9]{3}\\))|(\\+?[0-9]{3})) ?)?[0-9]{3,4}[ -]?[0-9]{4}$")
  private String phone1;
  @Pattern(regexp = "^(((\\(\\+?[0-9]{3}\\))|(\\+?[0-9]{3})) ?)?[0-9]{3,4}[ -]?[0-9]{4}$")
  private String phone2;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone1() {
    return phone1;
  }

  public void setPhone1(String phone1) {
    this.phone1 = phone1;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.id);
    hash = 79 * hash + Objects.hashCode(this.firstName);
    hash = 79 * hash + Objects.hashCode(this.lastName);
    hash = 79 * hash + Objects.hashCode(this.idNumber);
    hash = 79 * hash + Objects.hashCode(this.email);
    hash = 79 * hash + Objects.hashCode(this.phone1);
    hash = 79 * hash + Objects.hashCode(this.phone2);
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
    final PersonPojo other = (PersonPojo)obj;
    if (!Objects.equals(this.firstName, other.firstName)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.idNumber, other.idNumber)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.phone1, other.phone1)) {
      return false;
    }
    return Objects.equals(this.phone2, other.phone2);
  }

  @Override
  public String toString() {
    return "PersonPojo{id=" + id +
        ", firstName=" + firstName +
        ", lastName=" + lastName +
        ", idNumber=" + idNumber +
        ", email=" + email +
        ", phone1=" + phone1 +
        ", phone2=" + phone2 + '}';
  }

}
