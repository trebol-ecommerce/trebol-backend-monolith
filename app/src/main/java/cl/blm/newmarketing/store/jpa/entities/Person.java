package cl.blm.newmarketing.store.jpa.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.blm.newmarketing.store.jpa.GenericEntity;

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
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "person_name")
  private String name;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "person_idcard")
  private String idCard;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "person_email")
  private String email;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "person_address")
  private String address;
  @Basic(optional = false)
  @NotNull
  @Column(name = "person_phone1")
  private int phone1;
  @Basic(optional = false)
  @NotNull
  @Column(name = "person_phone2")
  private int phone2;

  public Person() {
  }

  public Person(Integer personId) {
    this.id = personId;
  }

  public Person(Integer personId, String personName, String personIdcard, String personEmail, String personAddress,
      int personPhone1, int personPhone2) {
    this.id = personId;
    this.name = personName;
    this.idCard = personIdcard;
    this.email = personEmail;
    this.address = personAddress;
    this.phone1 = personPhone1;
    this.phone2 = personPhone2;
  }

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

  public int getPhone1() {
    return phone1;
  }

  public void setPhone1(int phone1) {
    this.phone1 = phone1;
  }

  public int getPhone2() {
    return phone2;
  }

  public void setPhone2(int phone2) {
    this.phone2 = phone2;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Person)) {
      return false;
    }
    Person other = (Person) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.store.model.entities.Person[ personId=" + id + " ]";
  }

}
