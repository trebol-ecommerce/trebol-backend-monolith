package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ProductFamilyPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public String name;
}
