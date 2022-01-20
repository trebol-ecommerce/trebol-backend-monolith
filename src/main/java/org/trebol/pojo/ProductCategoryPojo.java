/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

;
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
