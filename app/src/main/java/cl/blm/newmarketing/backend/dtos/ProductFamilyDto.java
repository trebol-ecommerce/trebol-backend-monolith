package cl.blm.newmarketing.backend.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ProductFamilyDto {
  private Integer productFamilyId;
  private String productFamilyName;

  public ProductFamilyDto() {
    super();
  }

  public Integer getProductFamilyId() {
    return productFamilyId;
  }

  public void setProductFamilyId(Integer productFamilyId) {
    this.productFamilyId = productFamilyId;
  }

  public String getProductFamilyName() {
    return productFamilyName;
  }

  public void setProductFamilyName(String productFamilyName) {
    this.productFamilyName = productFamilyName;
  }
}
