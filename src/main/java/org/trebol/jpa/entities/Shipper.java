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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(
  name = "shippers",
  uniqueConstraints = @UniqueConstraint(columnNames = {"shipper_name"}))
@NamedQueries({ @NamedQuery(name = "Shipper.findAll", query = "SELECT s FROM Shipper s") })
public class Shipper
  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "shipper_id")
  private Long id;
  @Basic(optional = false)
  @Column(name = "shipper_name")
  private String name;

  public Shipper() { }

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
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.id);
    hash = 37 * hash + Objects.hashCode(this.name);
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
    final Shipper other = (Shipper)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Shipper{id=" + id +
        ", name=" + name + '}';
  }

}
