package cl.blm.newmarketing.backend.api.pojo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ClientPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  @Valid
  public PersonPojo person;
}
