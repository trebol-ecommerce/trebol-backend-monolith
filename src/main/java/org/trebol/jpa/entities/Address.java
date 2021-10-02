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
  name = "addresses",
  indexes = {
    @Index(columnList = "address_first_line"),
    @Index(columnList = "address_second_line"),
    @Index(columnList = "address_postal_code")
  },
  uniqueConstraints = @UniqueConstraint(columnNames = {
    "address_city", "address_municipality", "address_first_line",
    "address_second_line", "address_postal_code", "address_notes"}))
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

  public Address() { }

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
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + Objects.hashCode(this.city);
    hash = 71 * hash + Objects.hashCode(this.municipality);
    hash = 71 * hash + Objects.hashCode(this.firstLine);
    hash = 71 * hash + Objects.hashCode(this.secondLine);
    hash = 71 * hash + Objects.hashCode(this.postalCode);
    hash = 71 * hash + Objects.hashCode(this.notes);
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
    final Address other = (Address)obj;
    if (!Objects.equals(this.city, other.city)) {
      return false;
    }
    if (!Objects.equals(this.municipality, other.municipality)) {
      return false;
    }
    if (!Objects.equals(this.firstLine, other.firstLine)) {
      return false;
    }
    if (!Objects.equals(this.secondLine, other.secondLine)) {
      return false;
    }
    if (!Objects.equals(this.postalCode, other.postalCode)) {
      return false;
    }
    return Objects.equals(this.notes, other.notes);
  }

  @Override
  public String toString() {
    return "Address{id=" + id +
        ", city=" + city +
        ", municipality=" + municipality +
        ", firstLine=" + firstLine +
        ", secondLine=" + secondLine +
        ", postalCode=" + postalCode +
        ", notes=" + notes + '}';
  }
}
