package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
  public int hashCode() {
    int hash = 3;
    hash = 43 * hash + Objects.hashCode(this.id);
    hash = 43 * hash + Objects.hashCode(this.person);
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
    final Salesperson other = (Salesperson)obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return Objects.equals(this.person, other.person);
  }

  @Override
  public String toString() {
    return "Salesperson{id=" + id +
        ", person=" + person + '}';
  }

}
