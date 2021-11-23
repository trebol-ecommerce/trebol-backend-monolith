package org.trebol.pojo;

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
  @NotBlank
  private String code;
  @NotBlank
  private String name;
  @JsonInclude(NON_NULL)
  private ProductCategoryPojo parent;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductCategoryPojo that = (ProductCategoryPojo) o;
    return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(parent, that.parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, name, parent);
  }

  @Override
  public String toString() {
    return "ProductCategoryPojo{" +
        "code='" + code + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
