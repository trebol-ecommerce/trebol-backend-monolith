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
@NamedQueries({ @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p") })
public class Product
  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "product_id")
  private Long id;
  @Basic(optional = false)
  @Size(min = 1, max = 200)
  @Column(name = "product_name")
  private String name;
  @Size(max = 50)
  @Column(name = "product_code")
  private String barcode;
  @Basic(optional = false)
  @Column(name = "product_price")
  private int price;
  @Basic(optional = false)
  @Column(name = "product_stock_current")
  private int stockCurrent;
  @Basic(optional = false)
  @Column(name = "product_stock_critical")
  private int stockCritical;
  @JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id", insertable = true, updatable = true)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private ProductCategory productCategory;

  public Product() { }

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
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.id);
    hash = 11 * hash + Objects.hashCode(this.name);
    hash = 11 * hash + Objects.hashCode(this.barcode);
    hash = 11 * hash + this.price;
    hash = 11 * hash + this.stockCurrent;
    hash = 11 * hash + this.stockCritical;
    hash = 11 * hash + Objects.hashCode(this.productCategory);
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
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.productCategory, other.productCategory)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Product{id=" + id +
        ", name=" + name +
        ", barcode=" + barcode +
        ", price=" + price +
        ", stockCurrent=" + stockCurrent +
        ", stockCritical=" + stockCritical +
        ", productCategory=" + productCategory + '}';
  }

}
