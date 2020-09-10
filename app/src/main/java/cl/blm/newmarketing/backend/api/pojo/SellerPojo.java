package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SellerPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  @Valid
  private PersonPojo person;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public PersonPojo getPerson() {
    return person;
  }

  public void setPerson(PersonPojo person) {
    this.person = person;
  }

}