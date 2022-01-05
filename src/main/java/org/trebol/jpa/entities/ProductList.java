package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "product_lists",
  uniqueConstraints = @UniqueConstraint(columnNames = {"product_list_name"}))
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
  @Column(name = "product_list_disabled", nullable = false)
  private boolean disabled;
  @JoinColumn(name = "product_list_id")
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<ProductListItem> items;

  public ProductList() {
    this.disabled = true;
  }

  public ProductList(ProductList source) {
    this.id = source.id;
    this.name = source.name;
    this.disabled = source.disabled;
    this.items = source.items;
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

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public Collection<ProductListItem> getItems() {
    return items;
  }

  public void setItems(Collection<ProductListItem> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductList that = (ProductList) o;
    return Objects.equals(id, that.id) &&
        name.equals(that.name) &&
        disabled == that.disabled &&
        items.equals(that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, disabled, items);
  }

  @Override
  public String toString() {
    return "ProductList{id=" + id +
        ", name='" + name + '\'' +
        ", disabled=" + disabled +
        ", items=" + items +
        '}';
  }
}
