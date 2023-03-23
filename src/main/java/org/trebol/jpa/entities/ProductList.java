/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "product_lists",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_list_name"}),
    @UniqueConstraint(columnNames = {"product_list_code"}),
  })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
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
    this.items = source.items.stream()
      .map(sourceItem -> {
        ProductListItem target = new ProductListItem(sourceItem);
        target.setList(this);
        return target;
      })
      .collect(Collectors.toList());
  }
}
