package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "app_params")
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
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.id);
    hash = 67 * hash + Objects.hashCode(this.category);
    hash = 67 * hash + Objects.hashCode(this.name);
    hash = 67 * hash + Objects.hashCode(this.value);
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
    final Param other = (Param)obj;
    if (!Objects.equals(this.category, other.category)) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {
    return "Param{id=" + id +
        ", category=" + category +
        ", name=" + name +
        ", value=" + value + '}';
  }

}
