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

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class ProductListPojo {
  @JsonIgnore
  private Long id;
  private String name;
  @NotEmpty
  private String code;
  @JsonInclude(NON_DEFAULT)
  private long totalCount;

  public ProductListPojo() { }

  public ProductListPojo(String code) {
    this.code = code;
  }

  public ProductListPojo(Long id, String name, String code, long totalCount) {
    this.id = id;
    this.name = name;
    this.code = code;
    this.totalCount = totalCount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductListPojo that = (ProductListPojo) o;
    return totalCount == that.totalCount &&
        Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, totalCount);
  }

  @Override
  public String toString() {
    return "ProductListPojo{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", code='" + code + '\'' +
        ", totalCount=" + totalCount +
        '}';
  }
}
