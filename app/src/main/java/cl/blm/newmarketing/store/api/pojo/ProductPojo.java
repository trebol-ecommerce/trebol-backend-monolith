package cl.blm.newmarketing.store.api.pojo;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ProductPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private String name;
  @JsonInclude
  @NotNull
  private String barcode;
  @JsonInclude
  @NotNull
  private Integer price;
  @JsonInclude
  @NotNull
  private ProductTypePojo productType;
  @JsonInclude(value = Include.NON_EMPTY)
  private String description;
  @JsonInclude(value = Include.NON_EMPTY)
  private Integer currentStock;
  @JsonInclude(value = Include.NON_EMPTY)
  private Integer criticalStock;
  @JsonInclude(value = Include.NON_NULL)
  private Collection<String> imagesURL;

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

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public ProductTypePojo getProductType() {
    return productType;
  }

  public void setProductType(ProductTypePojo productType) {
    this.productType = productType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getCurrentStock() {
    return currentStock;
  }

  public void setCurrentStock(Integer currentStock) {
    this.currentStock = currentStock;
  }

  public Integer getCriticalStock() {
    return criticalStock;
  }

  public void setCriticalStock(Integer criticalStock) {
    this.criticalStock = criticalStock;
  }

  public Collection<String> getImagesURL() {
    return imagesURL;
  }

  public void setImagesURL(Collection<String> imagesURL) {
    this.imagesURL = imagesURL;
  }

}
