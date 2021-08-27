package org.trebol.api.pojo;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

//TODO could a user pojo benefit from person pojo?
/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class RegistrationPojo {
  @NotEmpty
  private String name;
  @NotEmpty
  private String password;
  @NotNull
  private RegistrationPojo.Profile profile;

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

  public RegistrationPojo.Profile getProfile() {
    return profile;
  }

  public void setProfile(RegistrationPojo.Profile profile) {
    this.profile = profile;
  }

  public class Profile {
    private String name;
    private String idNumber;
    private String email;
    private String address;
    private Integer phone1;
    private Integer phone2;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
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
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.name);
    hash = 19 * hash + Objects.hashCode(this.password);
    hash = 19 * hash + Objects.hashCode(this.profile);
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
    return "RegistrationPojo{name=" + name + ", password=" + password + ", profile=" + profile + '}';
  }
}
