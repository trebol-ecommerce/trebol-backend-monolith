package cl.blm.newmarketing.backend.api.pojo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellDetailPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public int units;
  @JsonInclude
  @NotNull
  public ProductPojo product;
  @JsonInclude(value = Include.NON_EMPTY)
  @Nullable
  public SellPojo sell;
}
