package cl.blm.newmarketing.backend.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellTypePojo {
  @JsonInclude
  public Integer id;
  @JsonInclude(value = Include.NON_DEFAULT)
  public String name;
}
