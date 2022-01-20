/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
