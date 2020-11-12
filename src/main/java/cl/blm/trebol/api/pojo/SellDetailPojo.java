package cl.blm.trebol.api.pojo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class SellDetailPojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private int units;
  @JsonInclude
  @NotNull
  private ProductPojo product;
  @JsonInclude(value = Include.NON_EMPTY)
  @Nullable
  private SellPojo sell;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
  }

  public ProductPojo getProduct() {
    return product;
  }

  public void setProduct(ProductPojo product) {
    this.product = product;
  }

  public SellPojo getSell() {
    return sell;
  }

  public void setSell(SellPojo sell) {
    this.sell = sell;
  }

}
