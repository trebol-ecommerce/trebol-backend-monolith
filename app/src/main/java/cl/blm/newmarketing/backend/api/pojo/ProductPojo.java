package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductPojo {
  @JsonInclude
  public Integer id;
  @JsonInclude
  public String name;
  @JsonInclude
  public String barcode;
  @JsonInclude
  public Integer price;
  @JsonInclude
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
