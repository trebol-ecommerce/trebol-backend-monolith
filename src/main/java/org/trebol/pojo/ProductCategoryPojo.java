package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class ProductCategoryPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String code;
  @NotBlank
  private String name;
  @JsonInclude(NON_NULL)
  private ProductCategoryPojo parent;

  public ProductCategoryPojo() { }

  public ProductCategoryPojo(String code) {
    this.code = code;
  }

  public ProductCategoryPojo(String code, String name, ProductCategoryPojo parent) {
    this.code = code;
    this.name = name;
    this.parent = parent;
  }

  public ProductCategoryPojo(Long id, String code, String name, ProductCategoryPojo parent) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.parent = parent;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
    return Objects.equals(id, that.id) &&
        Objects.equals(code, that.code) &&
        Objects.equals(name, that.name) &&
        Objects.equals(parent, that.parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, parent);
  }

  @Override
  public String toString() {
    return "ProductCategoryPojo{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", name='" + name + '\'' +
        ", parent=" + parent +
        '}';
  }
}
