package org.trebol.api.pojo;

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
    private String idCard;
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
  }
}
