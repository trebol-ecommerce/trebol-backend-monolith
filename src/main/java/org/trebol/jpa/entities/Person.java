package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(
  name = "people",
  indexes = {
    @Index(columnList = "person_id_number"),
    @Index(columnList = "person_email")
  },
  uniqueConstraints = @UniqueConstraint(columnNames = {"person_id_number"}))
public class Person
  implements Serializable {

  private static final long serialVersionUID = 9L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "person_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 200)
  @Column(name = "person_name", nullable = false)
  private String name;
  @Size(min = 1, max = 20)
  @Column(name = "person_id_number", nullable = false)
  private String idNumber;
  @Size(min = 5, max = 100)
  @Column(name = "person_email", nullable = false)
  private String email;
  @Column(name = "person_phone1", nullable = false)
  private String phone1 = "";
  @Column(name = "person_phone2", nullable = false)
  private String phone2 = "";

  public Person() { }

  public Person(Person source) {
    this.id = source.id;
    this.name = source.name;
    this.idNumber = source.idNumber;
    this.email = source.email;
    this.phone1 = source.phone1;
    this.phone2 = source.phone2;
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

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone1() {
    return phone1;
  }

  public void setPhone1(String phone1) {
    this.phone1 = phone1;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + Objects.hashCode(this.id);
    hash = 71 * hash + Objects.hashCode(this.name);
    hash = 71 * hash + Objects.hashCode(this.idNumber);
    hash = 71 * hash + Objects.hashCode(this.email);
    hash = 71 * hash + Objects.hashCode(this.phone1);
    hash = 71 * hash + Objects.hashCode(this.phone2);
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
    final Person other = (Person)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.idNumber, other.idNumber)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.phone1, other.phone1)) {
      return false;
    }
    return Objects.equals(this.phone2, other.phone2);
  }

  @Override
  public String toString() {
    return "Person{id=" + id +
        ", name=" + name +
        ", idNumber=" + idNumber +
        ", email=" + email +
        ", phone1=" + phone1 +
        ", phone2=" + phone2 + '}';
  }

}
