package cl.blm.newmarketing.backend.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ProductTypeDto {
  private Integer productTypeId;
  private String productTypeName;
  private ProductFamilyDto productFamily;

  public ProductTypeDto() {
    super();
  }

  public Integer getProductTypeId() {
    return productTypeId;
  }

  public void setProductTypeId(Integer productTypeId) {
    this.productTypeId = productTypeId;
  }

  public String getProductTypeName() {
    return productTypeName;
  }

  public void setProductTypeName(String productTypeName) {
    this.productTypeName = productTypeName;
  }

  public ProductFamilyDto getProductFamily() {
    return productFamily;
  }

  public void setProductFamily(ProductFamilyDto productFamily) {
    this.productFamily = productFamily;
  }
}
