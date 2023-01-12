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

package org.trebol.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product_lists",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_list_name"}),
    @UniqueConstraint(columnNames = {"product_list_code"}),
  })
@NoArgsConstructor
@Getter
@Setter
public class ProductList
  implements Serializable {
  private static final long serialVersionUID = 16L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_list_id", nullable = false)
  private Long id;
  @Size(min = 2, max = 50)
  @Column(name = "product_list_name", nullable = false)
  private String name;
  @Size(min = 2, max = 25)
  @Column(name = "product_list_code", nullable = false)
  private String code;
  @Column(name = "product_list_disabled", nullable = false)
  private boolean disabled;
  @OneToMany(mappedBy = "list")
  private List<ProductListItem> items;

  public ProductList(ProductList source) {
    this.id = source.id;
    this.name = source.name;
    this.code = source.code;
    this.disabled = source.disabled;
    this.items = source.items;
  }

  public ProductList(String code) {
    this.code = code;
  }

  public ProductList(String name, String code) {
    this.name = name;
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductList that = (ProductList) o;
    return disabled == that.disabled &&
      Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, disabled);
  }

  @Override
  public String toString() {
    return "ProductList{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", code='" + code + '\'' +
      ", disabled=" + disabled +
      '}';
  }
}
