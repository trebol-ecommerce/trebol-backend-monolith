package org.trebol.pojo;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class RegistrationPojo {
  @NotBlank
  private String name;
  @NotBlank
  private String password;
  @Valid
  private PersonPojo profile;

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

  public PersonPojo getProfile() {
    return profile;
  }

  public void setProfile(PersonPojo profile) {
    this.profile = profile;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.password);
    hash = 67 * hash + Objects.hashCode(this.profile);
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
    final RegistrationPojo other = (RegistrationPojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    if (!Objects.equals(this.profile, other.profile)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RegistrationPojo{name=" + name +
        ", password=" + password +
        ", profile=" + profile + '}';
  }
}
