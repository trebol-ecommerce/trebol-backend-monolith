package cl.blm.newmarketing.backend.model.entities;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.blm.newmarketing.backend.model.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "products")
@NamedQueries({ @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p") })
public class Product
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "product_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "product_name")
  private String name;
  @Size(max = 50)
  @Column(name = "product_code")
  private String barcode;
  @Basic(optional = false)
  @NotNull
  @Column(name = "product_price")
  private int price;
  @Basic(optional = false)
  @NotNull
  @Column(name = "product_stock_current")
  private int stockCurrent;
  @Basic(optional = false)
  @NotNull
  @Column(name = "product_stock_critical")
  private int stockCritical;
  @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id", insertable = true, updatable = true)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ProductType productType;

  public Product() {
  }

  public Product(Integer productId) {
    this.id = productId;
  }

  public Product(Integer productId, String productName, int productPrice, int productStockCurrent,
      int productStockCritical) {
    this.id = productId;
    this.name = productName;
    this.price = productPrice;
    this.stockCurrent = productStockCurrent;
    this.stockCritical = productStockCritical;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Product)) {
      return false;
    }
    Product other = (Product) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.backend.model.entities.Product[ productId=" + id + " ]";
  }

}
