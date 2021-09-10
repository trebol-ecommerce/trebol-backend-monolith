package org.trebol.api.pojo;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ProductCategoryPojo {
  private Long code;
  @NotBlank
  private String name;
  @JsonInclude(NON_NULL)
  private ProductCategoryPojo parent;

  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductCategoryPojo getParent() {
    return parent;
  }

  public void setParent(ProductCategoryPojo parent) {
    this.parent = parent;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + Objects.hashCode(this.code);
    hash = 23 * hash + Objects.hashCode(this.name);
    hash = 23 * hash + Objects.hashCode(this.parent);
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
    final ProductCategoryPojo other = (ProductCategoryPojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.code, other.code)) {
      return false;
    }
    if (!Objects.equals(this.parent, other.parent)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProductCategoryPojo{id=" + code +
        ", name=" + name +
        ", parent=" + parent + '}';
  }
}
