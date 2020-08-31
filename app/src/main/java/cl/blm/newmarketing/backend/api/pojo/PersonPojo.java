package cl.blm.newmarketing.backend.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PersonPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public String name;
  @JsonInclude
  public String idCard;
  @JsonInclude
  public String email;
  @JsonInclude(value = Include.NON_EMPTY)
  public String address;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer phone1;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer phone2;
}
