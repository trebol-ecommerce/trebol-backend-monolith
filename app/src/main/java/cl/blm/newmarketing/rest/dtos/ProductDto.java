package cl.blm.newmarketing.rest.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ProductDto {
  private Integer productId;
  private String productName;
  private String productCode;
  private Integer productPrice;
  private Integer productStockCurrent;
  private Integer productStockCritical;
  private ProductTypeDto productType;

  public ProductDto() {
    super();
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public Integer getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(Integer productPrice) {
    this.productPrice = productPrice;
  }

  public Integer getProductStockCurrent() {
    return productStockCurrent;
  }

  public void setProductStockCurrent(Integer productStockCurrent) {
    this.productStockCurrent = productStockCurrent;
  }

  public Integer getProductStockCritical() {
    return productStockCritical;
  }

  public void setProductStockCritical(Integer productStockCritical) {
    this.productStockCritical = productStockCritical;
  }

  public ProductTypeDto getProductType() {
    return productType;
  }

  public void setProductType(ProductTypeDto productType) {
    this.productType = productType;
  }
}
