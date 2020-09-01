package cl.blm.newmarketing.backend.api.pojo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductTypePojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public String name;
  @JsonInclude(value = Include.NON_EMPTY)
  @Nullable
  public ProductFamilyPojo productFamily;
}
