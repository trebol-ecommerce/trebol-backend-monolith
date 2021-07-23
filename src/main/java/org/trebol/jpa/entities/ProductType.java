package org.trebol.jpa.entities;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "products_types")
@NamedQueries({ @NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p") })
public class ProductType
    implements GenericEntity<Integer> {

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
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.id);
    hash = 37 * hash + Objects.hashCode(this.name);
    hash = 37 * hash + Objects.hashCode(this.productFamily);
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
    final ProductType other = (ProductType)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.productFamily, other.productFamily)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProductType{id=" + id +
        ", name=" + name +
        ", productFamily=" + productFamily + '}';
  }

}
