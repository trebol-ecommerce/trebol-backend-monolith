package org.trebol.api.pojo;

import java.util.Objects;

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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.id);
    hash = 59 * hash + Objects.hashCode(this.name);
    hash = 59 * hash + Objects.hashCode(this.productFamily);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ProductTypePojo other = (ProductTypePojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.productFamily, other.productFamily)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProductTypePojo{" + "id=" + id + ", name=" + name + ", productFamily=" + productFamily + '}';
  }

}
