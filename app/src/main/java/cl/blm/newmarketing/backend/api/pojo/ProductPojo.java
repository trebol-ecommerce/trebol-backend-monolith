package cl.blm.newmarketing.backend.api.pojo;

import java.util.Collection;

public class ProductPojo {
  public Integer id;
  public String name;
  public String barcode;
  public Integer price;
  public ProductTypePojo productType;
  public String description;
  public Integer currentStock;
  public Integer criticalStock;
  public Collection<String> imagesURL;
}
