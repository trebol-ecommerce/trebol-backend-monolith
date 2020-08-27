package cl.blm.newmarketing.backend.api.pojos;

import java.util.Collection;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
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
