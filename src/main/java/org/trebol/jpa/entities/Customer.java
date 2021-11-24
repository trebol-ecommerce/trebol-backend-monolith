package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "customers")
public class Customer
  implements Serializable {

  private static final long serialVersionUID = 4L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id", nullable = false)
  private Long id;
  @JoinColumn(name = "person_id", referencedColumnName = "person_id")
  @OneToOne(optional = false, cascade = CascadeType.ALL)
  private Person person;

  public Customer() { }

  public Customer(Customer source) {
    this.id = source.id;
    this.person = source.person;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id) &&
        Objects.equals(person, customer.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, person);
  }

  @Override
  public String toString() {
    return "Customer{" +
        "id=" + id +
        ", person=" + person +
        '}';
  }
}
