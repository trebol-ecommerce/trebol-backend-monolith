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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_list_items")
public class ProductListItem
  implements Serializable {
  private static final long serialVersionUID = 17L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_list_item_id", nullable = false)
  private Long id;
  @JoinColumn(name = "product_list_id", nullable = false)
  @ManyToOne(optional = false)
  private ProductList list;
  @JoinColumn(name = "product_id", nullable = false)
  @ManyToOne(optional = false)
  private Product product;

  public ProductListItem() {
  }

  public ProductListItem(ProductListItem source) {
    this.id = source.id;
    this.list = source.list;
    this.product = source.product;
  }

  public ProductListItem(ProductList list, Product product) {
    this.list = list;
    this.product = product;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProductList getList() {
    return list;
  }

  public void setList(ProductList list) {
    this.list = list;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductListItem that = (ProductListItem) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(list, that.list) &&
      Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, list, product);
  }

  @Override
  public String toString() {
    return "ProductListItem{id=" + id +
      ", product=" + product +
      '}';
  }
}
