package cl.blm.newmarketing.backend.api.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SellDetailPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public int units;
  @JsonInclude
  public ProductPojo product;
  @JsonInclude(value = Include.NON_EMPTY)
  public SellPojo sell;
}
