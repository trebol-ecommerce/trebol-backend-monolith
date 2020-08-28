package cl.blm.newmarketing.backend.model.entities;

import java.io.Serializable;

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

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "product_types")
@NamedQueries({ @NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p") })
public class ProductType
    implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "product_type_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "product_type_name")
  private String name;
  @JoinColumn(name = "product_family_id", referencedColumnName = "product_family_id", insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ProductFamily productFamily;

  public ProductType() {
  }

  public ProductType(Integer productTypeId) {
    this.id = productTypeId;
  }

  public ProductType(Integer productTypeId, String productTypeName) {
    this.id = productTypeId;
    this.name = productTypeName;
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

  public ProductFamily getProductFamily() {
    return productFamily;
  }

  public void setProductFamily(ProductFamily productFamily) {
    this.productFamily = productFamily;
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
    if (!(object instanceof ProductType)) {
      return false;
    }
    ProductType other = (ProductType) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.backend.model.entities.ProductType[ productTypeId=" + id + " ]";
  }

}
