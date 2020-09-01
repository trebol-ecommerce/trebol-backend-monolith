package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  @NotNull
  public String name;
  @JsonInclude
  @NotNull
  public String barcode;
  @JsonInclude
  @NotNull
  public Integer price;
  @JsonInclude
  @NotNull
  public ProductTypePojo productType;
  @JsonInclude(value = Include.NON_EMPTY)
  public String description;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer currentStock;
  @JsonInclude(value = Include.NON_EMPTY)
  public Integer criticalStock;
  @JsonInclude(value = Include.NON_EMPTY)
  public Collection<String> imagesURL;
}
