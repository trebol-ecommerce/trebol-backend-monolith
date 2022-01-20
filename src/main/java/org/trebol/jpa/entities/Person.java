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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
  name = "people",
  indexes = {
    @Index(columnList = "person_id_number"),
    @Index(columnList = "person_first_name"),
    @Index(columnList = "person_last_name"),
    @Index(columnList = "person_email")
  })
public class Person
  implements Serializable {

  private static final long serialVersionUID = 9L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "person_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 200)
  @Column(name = "person_first_name", nullable = false)
  private String firstName;
  @Size(min = 1, max = 200)
  @Column(name = "person_last_name", nullable = false)
  private String lastName;
  @Size(min = 1, max = 20)
  @Column(name = "person_id_number", nullable = false, unique = true)
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
    this.firstName = source.firstName;
    this.lastName = source.lastName;
    this.idNumber = source.idNumber;
    this.email = source.email;
    this.phone1 = source.phone1;
    this.phone2 = source.phone2;
  }

  public Person(String idNumber) {
    this.idNumber = idNumber;
  }

  public Person(String firstName, String lastName, String idNumber, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.idNumber = idNumber;
    this.email = email;
  }

  public Person(Long id,
                String firstName,
                String lastName,
                String idNumber,
                String email,
                String phone1,
                String phone2) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.idNumber = idNumber;
    this.email = email;
    this.phone1 = phone1;
    this.phone2 = phone2;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(id, person.id) &&
        Objects.equals(firstName, person.firstName) &&
        Objects.equals(lastName, person.lastName) &&
        Objects.equals(idNumber, person.idNumber) &&
        Objects.equals(email, person.email) &&
        Objects.equals(phone1, person.phone1) &&
        Objects.equals(phone2, person.phone2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, idNumber, email, phone1, phone2);
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", idNumber='" + idNumber + '\'' +
        ", email='" + email + '\'' +
        ", phone1='" + phone1 + '\'' +
        ", phone2='" + phone2 + '\'' +
        '}';
  }
}
