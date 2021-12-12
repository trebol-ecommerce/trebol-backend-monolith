package org.trebol.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "salespeople")
public class Salesperson
  implements Serializable {

  private static final long serialVersionUID = 13L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "salesperson_id", nullable = false)
  private Long id;
  @JoinColumn(name = "person_id", referencedColumnName = "person_id")
  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  private Person person;

  public Salesperson() { }

  public Salesperson(Salesperson source) {
    this.id = source.id;
    this.person = source.person;
  }

  public Salesperson(String idNumber) {
    this.person = new Person(idNumber);
  }

  public Salesperson(Person person) {
    this.person = person;
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
    Salesperson that = (Salesperson) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(person, that.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, person);
  }

  @Override
  public String toString() {
    return "Salesperson{" +
        "id=" + id +
        ", person=" + person +
        '}';
  }
}
