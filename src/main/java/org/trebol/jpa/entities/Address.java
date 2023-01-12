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
  name = "addresses",
  indexes = {
    @Index(columnList = "address_first_line"),
    @Index(columnList = "address_second_line"),
    @Index(columnList = "address_postal_code")
  },
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {
      "address_city", "address_municipality", "address_first_line",
      "address_second_line", "address_postal_code", "address_notes"
    })
  })
public class Address
  implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id", nullable = false)
  private Long id;
  @Size(max = 50)
  @Column(name = "address_city", nullable = false)
  private String city;
  @Size(max = 50)
  @Column(name = "address_municipality", nullable = false)
  private String municipality;
  @Size(max = 100)
  @Column(name = "address_first_line", nullable = false)
  private String firstLine;
  @Size(max = 50)
  @Column(name = "address_second_line")
  private String secondLine;
  @Column(name = "address_postal_code")
  private String postalCode;
  @Size(max = 50)
  @Column(name = "address_notes")
  private String notes;

  public Address() {
  }

  public Address(Address source) {
    this.id = source.id;
    this.city = source.city;
    this.municipality = source.municipality;
    this.firstLine = source.firstLine;
    this.secondLine = source.secondLine;
    this.postalCode = source.postalCode;
    this.notes = source.notes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getMunicipality() {
    return municipality;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public String getFirstLine() {
    return firstLine;
  }

  public void setFirstLine(String firstLine) {
    this.firstLine = firstLine;
  }

  public String getSecondLine() {
    return secondLine;
  }

  public void setSecondLine(String secondLine) {
    this.secondLine = secondLine;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(id, address.id) &&
      Objects.equals(city, address.city) &&
      Objects.equals(municipality, address.municipality) &&
      Objects.equals(firstLine, address.firstLine) &&
      Objects.equals(secondLine, address.secondLine) &&
      Objects.equals(postalCode, address.postalCode) &&
      Objects.equals(notes, address.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, city, municipality, firstLine, secondLine, postalCode, notes);
  }

  @Override
  public String toString() {
    return "Address{" +
      "id=" + id +
      ", city='" + city + '\'' +
      ", municipality='" + municipality + '\'' +
      ", firstLine='" + firstLine + '\'' +
      ", secondLine='" + secondLine + '\'' +
      ", postalCode='" + postalCode + '\'' +
      ", notes='" + notes + '\'' +
      '}';
  }
}
