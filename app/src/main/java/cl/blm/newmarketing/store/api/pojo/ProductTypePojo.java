package cl.blm.newmarketing.store.api.pojo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ProductTypePojo {
  @JsonInclude
  private Integer id;
  @JsonInclude
  @NotNull
  private String name;
  @JsonInclude(value = Include.NON_EMPTY)
  @Nullable
  private ProductFamilyPojo productFamily;

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

  public ProductFamilyPojo getProductFamily() {
    return productFamily;
  }

  public void setProductFamily(ProductFamilyPojo productFamily) {
    this.productFamily = productFamily;
  }

}
