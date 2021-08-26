package org.trebol.api.pojo;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CustomerPojo {
  @JsonInclude
  private Long id;
  @JsonInclude
  @NotNull
  @Valid
  private PersonPojo person;

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
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.id);
    hash = 67 * hash + Objects.hashCode(this.person);
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
    final CustomerPojo other = (CustomerPojo)obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.person, other.person)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CustomerPojo{" + "id=" + id + ", person=" + person + '}';
  }

}
