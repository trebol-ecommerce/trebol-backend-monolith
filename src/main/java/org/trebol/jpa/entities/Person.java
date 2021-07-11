package org.trebol.jpa.entities;

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

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "people")
@NamedQueries({ @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p") })
public class Person
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "person_id")
  private Integer id;
  @Basic(optional = false)
  @Size(min = 1, max = 200)
  @Column(name = "person_name")
  private String name;
  @Basic(optional = false)
  @Size(min = 1, max = 20)
  @Column(name = "person_idcard")
  private String idCard;
  @Basic(optional = false)
  @Size(min = 1, max = 100)
  @Column(name = "person_email")
  private String email;
  @Basic(optional = true)
  @Size(max = 200)
  @Column(name = "person_address")
  private String address;
  @Basic(optional = true)
  @Column(name = "person_phone1")
  private Integer phone1;
  @Basic(optional = true)
  @Column(name = "person_phone2")
  private Integer phone2;

  public Person() {
  }

  public Person(Integer personId) {
    this.id = personId;
  }

  public Person(
    Integer personId,
    String personName,
    String personIdcard,
    String personEmail,
    String personAddress,
    Integer personPhone1,
    Integer personPhone2
  ) {
    this.id = personId;
    this.name = personName;
    this.idCard = personIdcard;
    this.email = personEmail;
    this.address = personAddress;
    this.phone1 = personPhone1;
    this.phone2 = personPhone2;
  }

  @Override
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPhone1() {
    return phone1;
  }

  public void setPhone1(Integer phone1) {
    this.phone1 = phone1;
  }

  public Integer getPhone2() {
    return phone2;
  }

  public void setPhone2(Integer phone2) {
    this.phone2 = phone2;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 83 * hash + Objects.hashCode(this.idCard);
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
    return !Objects.equals(this.idCard, other.idCard);
  }

  @Override
  public String toString() {
    return "Person{id=" + id +
        ", name=" + name +
        ", idCard=" + idCard +
        ", email=" + email +
        ", address=" + address +
        ", phone1=" + phone1 +
        ", phone2=" + phone2 + '}';
  }

}
