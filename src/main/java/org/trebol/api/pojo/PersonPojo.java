package org.trebol.api.pojo;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class PersonPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private String name;
  @JsonInclude
  @NotNull
  private String idCard;
  @JsonInclude
  @NotNull
  private String email;
  @JsonInclude(value = Include.NON_EMPTY)
  @NotEmpty
  private String address;
  @JsonInclude(value = Include.NON_EMPTY)
  private Integer phone1;
  @JsonInclude(value = Include.NON_EMPTY)
  private Integer phone2;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPhone1() {
    return phone1;
  }

  public void setPhone1(Integer phone1) {
    this.phone1 = phone1;
  }

  public Integer getPhone2() {
    return phone2;
  }

  public void setPhone2(Integer phone2) {
    this.phone2 = phone2;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 79 * hash + Objects.hashCode(this.id);
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Objects.hashCode(this.idCard);
    hash = 79 * hash + Objects.hashCode(this.email);
    hash = 79 * hash + Objects.hashCode(this.address);
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
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.idCard, other.idCard)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.address, other.address)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.phone1, other.phone1)) {
      return false;
    }
    if (!Objects.equals(this.phone2, other.phone2)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PersonPojo{" + "id=" + id + ", name=" + name + ", idCard=" + idCard + ", email=" + email + ", address=" + address + ", phone1=" + phone1 + ", phone2=" + phone2 + '}';
  }

}
