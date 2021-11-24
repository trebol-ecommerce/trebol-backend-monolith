package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "billing_types")
public class BillingType
  implements Serializable {

  private static final long serialVersionUID = 3L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "billing_type_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 100)
  @Column(name = "billing_type_name", nullable = false, unique = true)
  private String name;

  public BillingType() { }

  public BillingType(BillingType source) {
    this.id = source.id;
    this.name = source.name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BillingType that = (BillingType) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "BillingType{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
