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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
  name = "products",
  indexes = {
    @Index(columnList = "product_name")
  })
public class Product
  implements Serializable {

  private static final long serialVersionUID = 10L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id", nullable = false)
  private Long id;
  @Size(max = 200)
  @Column(name = "product_name", nullable = false, unique = true)
  private String name;
  @Size(max = 50)
  @Column(name = "product_code", nullable = false, unique = true)
  private String barcode;
  @Size(max = 4000)
  @Column(name = "product_description")
  private String description;
  @Column(name = "product_price", nullable = false)
  private int price;
  @Column(name = "product_stock_current", nullable = false)
  private int stockCurrent;
  @Column(name = "product_stock_critical", nullable = false)
  private int stockCritical;
  @JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private ProductCategory productCategory;

  public Product() { }

  public Product(Product source) {
    this.id = source.id;
    this.name = source.name;
    this.barcode = source.barcode;
    this.description = source.description;
    this.price = source.price;
    this.stockCurrent = source.stockCurrent;
    this.stockCritical = source.stockCritical;
    this.productCategory = source.productCategory;
  }

  public Product(String barcode) {
    this.barcode = barcode;
  }

  public Product(Long id,
                 String name,
                 String barcode,
                 String description,
                 int price,
                 int stockCurrent,
                 int stockCritical,
                 ProductCategory productCategory) {
    this.id = id;
    this.name = name;
    this.barcode = barcode;
    this.description = description;
    this.price = price;
    this.stockCurrent = stockCurrent;
    this.stockCritical = stockCritical;
    this.productCategory = productCategory;
  }

  public Product(String name,
                 String barcode,
                 String description,
                 int price,
                 int stockCurrent,
                 int stockCritical) {
    this.name = name;
    this.barcode = barcode;
    this.description = description;
    this.price = price;
    this.stockCurrent = stockCurrent;
    this.stockCritical = stockCritical;
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

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getStockCurrent() {
    return stockCurrent;
  }

  public void setStockCurrent(int stockCurrent) {
    this.stockCurrent = stockCurrent;
  }

  public int getStockCritical() {
    return stockCritical;
  }

  public void setStockCritical(int stockCritical) {
    this.stockCritical = stockCritical;
  }

  public ProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return price == product.price &&
        stockCurrent == product.stockCurrent &&
        stockCritical == product.stockCritical &&
        Objects.equals(id, product.id) &&
        Objects.equals(name, product.name) &&
        Objects.equals(barcode, product.barcode) &&
        Objects.equals(description, product.description) &&
        Objects.equals(productCategory, product.productCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, barcode, description, price, stockCurrent, stockCritical, productCategory);
  }

  @Override
  public String toString() {
    return "Product{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", barcode='" + barcode + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", stockCurrent=" + stockCurrent +
        ", stockCritical=" + stockCritical +
        ", productCategory=" + productCategory +
        '}';
  }
}
