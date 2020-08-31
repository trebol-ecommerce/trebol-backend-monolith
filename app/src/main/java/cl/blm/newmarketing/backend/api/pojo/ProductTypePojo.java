package cl.blm.newmarketing.backend.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductTypePojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public String name;
  @JsonInclude(value = Include.NON_EMPTY)
  public ProductFamilyPojo productFamily;
}
