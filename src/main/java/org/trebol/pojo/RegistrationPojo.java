package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistrationPojo that = (RegistrationPojo) o;
    return Objects.equals(name, that.name) && Objects.equals(password, that.password) && Objects.equals(profile, that.profile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, password, profile);
  }

  @Override
  public String toString() {
    return "RegistrationPojo{" +
        "name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", profile=" + profile +
        '}';
  }
}
