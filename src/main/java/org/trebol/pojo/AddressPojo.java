package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AddressPojo that = (AddressPojo) o;
    return Objects.equals(firstLine, that.firstLine) &&
        Objects.equals(secondLine, that.secondLine) &&
        Objects.equals(municipality, that.municipality) &&
        Objects.equals(city, that.city) &&
        Objects.equals(postalCode, that.postalCode) &&
        Objects.equals(notes, that.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstLine, secondLine, municipality, city, postalCode, notes);
  }

  @Override
  public String toString() {
    return "AddressPojo{" +
        "firstLine='" + firstLine + '\'' +
        ", secondLine='" + secondLine + '\'' +
        ", municipality='" + municipality + '\'' +
        ", city='" + city + '\'' +
        ", postalCode='" + postalCode + '\'' +
        ", notes='" + notes + '\'' +
        '}';
  }
}
