package org.trebol.pojo;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_EMPTY)
public class AddressPojo {
  @NotBlank
  private String firstLine;
  private String secondLine;
  @NotBlank
  private String municipality;
  @NotBlank
  private String city;
  private String postalCode;
  private String notes;

  public AddressPojo() { }

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

  public String getMunicipality() {
    return municipality;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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
    int hash = 5;
    hash = 19 * hash + Objects.hashCode(this.firstLine);
    hash = 19 * hash + Objects.hashCode(this.secondLine);
    hash = 19 * hash + Objects.hashCode(this.municipality);
    hash = 19 * hash + Objects.hashCode(this.city);
    hash = 19 * hash + Objects.hashCode(this.postalCode);
    hash = 19 * hash + Objects.hashCode(this.notes);
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
    final AddressPojo other = (AddressPojo)obj;
    if (!Objects.equals(this.firstLine, other.firstLine)) {
      return false;
    }
    if (!Objects.equals(this.secondLine, other.secondLine)) {
      return false;
    }
    if (!Objects.equals(this.municipality, other.municipality)) {
      return false;
    }
    if (!Objects.equals(this.city, other.city)) {
      return false;
    }
    if (!Objects.equals(this.postalCode, other.postalCode)) {
      return false;
    }
    if (!Objects.equals(this.notes, other.notes)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AddressPojo{firstLine=" + firstLine +
        ", secondLine=" + secondLine +
        ", municipality=" + municipality +
        ", city=" + city +
        ", postalCode=" + postalCode +
        ", notes=" + notes + '}';
  }

  public String toFormattedString() {
    StringBuilder sb = new StringBuilder();
    if (postalCode != null && !postalCode.isBlank()) {
      sb.append(postalCode).append(" ");
    }
    sb.append(firstLine);
    if (secondLine != null && !secondLine.isBlank()) {
      sb.append(", ").append(secondLine);
    }
    sb.append(", ").append(municipality).append(", ").append(city);
    if (notes != null && !notes.isBlank()) {
      sb.append(" (Comentario: ").append(notes).append(")");
    }
    return sb.toString();
  }
}
