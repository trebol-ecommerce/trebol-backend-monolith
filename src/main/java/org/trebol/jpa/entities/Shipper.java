package org.trebol.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "shippers")
public class Shipper
  implements Serializable {

  private static final long serialVersionUID = 18L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shipper_id", nullable = false)
  private Long id;
  @Column(name = "shipper_name", nullable = false, unique = true)
  private String name;

  public Shipper() { }

  public Shipper(Shipper source) {
    this.id = source.id;
    this.name = source.name;
  }

  public Shipper(Long id, String name) {
    this.id = id;
    this.name = name;
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
    Shipper shipper = (Shipper) o;
    return Objects.equals(id, shipper.id) &&
        Objects.equals(name, shipper.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "Shipper{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
