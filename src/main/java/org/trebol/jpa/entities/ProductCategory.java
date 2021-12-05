package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(
  name = "products_categories",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"parent_product_category_id", "product_category_name"})
  })
public class ProductCategory
  implements Serializable {

  private static final long serialVersionUID = 11L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_category_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 50)
  @Column(name = "product_category_code", nullable = false, unique = true)
  private String code;
  @Size(min = 1, max = 100)
  @Column(name = "product_category_name", nullable = false)
  private String name;
  @JoinColumn(name = "parent_product_category_id", referencedColumnName = "product_category_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private ProductCategory parent;

  public ProductCategory() { }

  public ProductCategory(ProductCategory source) {
    this.id = source.id;
    this.code = source.code;
    this.name = source.name;
    this.parent = source.parent;
  }

  public ProductCategory(Long id, String code, String name, ProductCategory parent) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.parent = parent;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductCategory that = (ProductCategory) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(code, that.code) &&
        Objects.equals(name, that.name) &&
        Objects.equals(parent, that.parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, parent);
  }

  @Override
  public String toString() {
    return "ProductCategory{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", name='" + name + '\'' +
        ", parent=" + parent +
        '}';
  }
}
