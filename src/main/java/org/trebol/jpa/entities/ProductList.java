package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_lists",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_list_name"}),
    @UniqueConstraint(columnNames = {"product_list_code"}),
  })
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
  @Size(min = 2, max = 25)
  @Column(name = "product_list_code", nullable = false)
  private String code;
  @Column(name = "product_list_disabled", nullable = false)
  private boolean disabled;

  public ProductList() {
    this.disabled = true;
  }

  public ProductList(ProductList source) {
    this.id = source.id;
    this.name = source.name;
    this.code = source.code;
    this.disabled = source.disabled;
  }

  public ProductList(String code) {
    this.code = code;
  }

  public ProductList(String name, String code) {
    this.name = name;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductList that = (ProductList) o;
    return disabled == that.disabled &&
        Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, disabled);
  }

  @Override
  public String toString() {
    return "ProductList{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", code='" + code + '\'' +
        ", disabled=" + disabled +
        '}';
  }
}
