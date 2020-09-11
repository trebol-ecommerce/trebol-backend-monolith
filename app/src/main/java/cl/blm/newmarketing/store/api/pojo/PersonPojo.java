package cl.blm.newmarketing.store.api.pojo;

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

}
