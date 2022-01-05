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

  public ProductListItem() { }

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
