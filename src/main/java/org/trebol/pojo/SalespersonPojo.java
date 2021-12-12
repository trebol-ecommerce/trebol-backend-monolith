package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class SalespersonPojo {
  @JsonIgnore
  private Long id;
  @NotNull
  @Valid
  private PersonPojo person;

  public SalespersonPojo() { }

  public SalespersonPojo(String idNumber) {
    this.person = new PersonPojo(idNumber);
  }

  public SalespersonPojo(PersonPojo person) {
    this.person = person;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PersonPojo getPerson() {
    return person;
  }

  public void setPerson(PersonPojo person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SalespersonPojo that = (SalespersonPojo) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(person, that.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, person);
  }

  @Override
  public String toString() {
    return "SalespersonPojo{" +
        "id=" + id +
        ", person=" + person +
        '}';
  }
}
