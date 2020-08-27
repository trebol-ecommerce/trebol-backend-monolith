package cl.blm.newmarketing.backend.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellDetailDto {
  private Integer sellDetailId;
  private int sellDetailUnits;
  private SellDto sell;
  private ProductDto product;

  public SellDetailDto() {
    super();
  }

  public Integer getSellDetailId() {
    return sellDetailId;
  }

  public void setSellDetailId(Integer sellDetailId) {
    this.sellDetailId = sellDetailId;
  }

  public int getSellDetailUnits() {
    return sellDetailUnits;
  }

  public void setSellDetailUnits(int sellDetailUnits) {
    this.sellDetailUnits = sellDetailUnits;
  }

  public SellDto getSell() {
    return sell;
  }

  public void setSell(SellDto sell) {
    this.sell = sell;
  }

  public ProductDto getProduct() {
    return product;
  }

  public void setProduct(ProductDto product) {
    this.product = product;
  }
}
