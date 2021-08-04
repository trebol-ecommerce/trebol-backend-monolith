package org.trebol.jpa.entities;

import java.util.Objects;

import javax.annotation.Nullable;
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

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "products_categories")
@NamedQueries({ @NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p") })
public class ProductCategory
    implements GenericEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "product_category_id")
  private Long id;
  @Basic(optional = false)
  @Size(min = 1, max = 100)
  @Column(name = "product_category_name")
  private String name;
  @JoinColumn(name = "parent_product_category_id", referencedColumnName = "product_category_id", insertable = true, updatable = true)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private ProductCategory parent;

  public ProductCategory() {
  }

  public ProductCategory(Long id, String name, ProductCategory parent) {
    this.id = id;
    this.name = name;
    this.parent = parent;
  }

  @Override
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

  public ProductCategory getParent() {
    return parent;
  }

  public void setParent(ProductCategory parent) {
    this.parent = parent;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.parent);
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
    final ProductCategory other = (ProductCategory)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.parent, other.parent)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ProductCategory{id=" + id +
        ", name=" + name +
        ", parent=" + parent + '}';
  }

}
