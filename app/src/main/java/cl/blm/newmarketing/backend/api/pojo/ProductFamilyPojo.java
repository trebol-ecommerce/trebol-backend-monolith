package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ProductFamilyPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private String name;

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

}
