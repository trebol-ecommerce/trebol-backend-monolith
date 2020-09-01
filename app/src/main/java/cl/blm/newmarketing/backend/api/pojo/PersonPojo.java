package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PersonPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public String name;
  @JsonInclude
  @NotNull
  public String idCard;
  @JsonInclude
  @NotNull
  public String email;
  @JsonInclude(value = Include.NON_EMPTY)
  @NotEmpty
  public String address;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer phone1;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer phone2;
}
