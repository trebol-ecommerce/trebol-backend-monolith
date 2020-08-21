package cl.blm.newmarketing.backend.pojos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonSubTypes(value = { @Type(BasePojo.class) })
@JsonTypeInfo(use = Id.NONE)
public class BasePojo {
  public Integer id;
  public String name;
}
