package org.trebol.pojo;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LoginPojo {
  @NotBlank
  private String name;
  @NotBlank
  private String password;

  public LoginPojo() {
    super();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LoginPojo loginPojo = (LoginPojo) o;
    return Objects.equals(name, loginPojo.name) && Objects.equals(password, loginPojo.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, password);
  }

  @Override
  public String toString() {
    return "LoginPojo{" +
        "name='" + name + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
