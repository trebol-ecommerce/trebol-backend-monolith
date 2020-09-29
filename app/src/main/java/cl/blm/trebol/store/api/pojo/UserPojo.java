package cl.blm.trebol.store.api.pojo;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//TODO could a user pojo benefit from person pojo?
/**
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class UserPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude(value = Include.NON_DEFAULT)
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
