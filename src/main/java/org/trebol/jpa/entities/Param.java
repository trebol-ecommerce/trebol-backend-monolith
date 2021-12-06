package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(
  name = "app_params",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"param_category", "param_name"})
  })
public class Param
  implements Serializable {

  private static final long serialVersionUID = 6L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "param_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 25)
  @Column(name = "param_category", nullable = false)
  private String category;
  @Size(min = 1, max = 50)
  @Column(name = "param_name", nullable = false)
  private String name;
  @Size(min = 1, max = 500)
  @Column(name = "param_value", nullable = false)
  private String value;

  public Param() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Param param = (Param) o;
    return Objects.equals(id, param.id) &&
        Objects.equals(category, param.category) &&
        Objects.equals(name, param.name) &&
        Objects.equals(value, param.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, category, name, value);
  }

  @Override
  public String toString() {
    return "Param{" +
        "id=" + id +
        ", category='" + category + '\'' +
        ", name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
