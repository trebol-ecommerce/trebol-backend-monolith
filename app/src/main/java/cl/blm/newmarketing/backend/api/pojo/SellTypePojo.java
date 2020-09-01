package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellTypePojo {
  @JsonInclude
  public Integer id;
  @JsonInclude(value = Include.NON_DEFAULT)
  @NotNull
  public String name;
}
