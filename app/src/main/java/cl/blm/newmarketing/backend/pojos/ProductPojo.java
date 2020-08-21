package cl.blm.newmarketing.backend.pojos;

import java.util.Collection;

public class ProductPojo
    extends BasePojo {
  public String barcode;
  public Integer price;
  public FamilyTypePojo productType;
  public String description;
  public Integer currentStock;
  public Integer criticalStock;
  public Collection<String> imagesURL;
}
