package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "products")
public class Product
  implements Serializable {

  private static final long serialVersionUID = 10L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 200)
  @Column(name = "product_name", nullable = false)
  private String name;
  @Size(max = 50)
  @Column(name = "product_code")
  private String barcode;
  @Size(max = 1000)
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
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.id);
    hash = 79 * hash + Objects.hashCode(this.name);
    hash = 79 * hash + Objects.hashCode(this.barcode);
    hash = 79 * hash + Objects.hashCode(this.description);
    hash = 79 * hash + this.price;
    hash = 79 * hash + this.stockCurrent;
    hash = 79 * hash + this.stockCritical;
    hash = 79 * hash + Objects.hashCode(this.productCategory);
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
    final Product other = (Product)obj;
    if (this.price != other.price) {
      return false;
    }
    if (this.stockCurrent != other.stockCurrent) {
      return false;
    }
    if (this.stockCritical != other.stockCritical) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.barcode, other.barcode)) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.productCategory, other.productCategory);
  }

  @Override
  public String toString() {
    return "Product{id=" + id +
        ", name=" + name +
        ", barcode=" + barcode +
        ", description=" + description +
        ", price=" + price +
        ", stockCurrent=" + stockCurrent +
        ", stockCritical=" + stockCritical +
        ", productCategory=" + productCategory + '}';
  }

}
