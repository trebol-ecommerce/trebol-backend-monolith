package cl.blm.newmarketing.backend.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ClientPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public PersonPojo person;
}
