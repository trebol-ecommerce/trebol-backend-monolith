package org.trebol.api.pojo;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class PaymentTypePojo {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.name);
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
    final PaymentTypePojo other = (PaymentTypePojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "PaymentTypePojo{name=" + name + '}';
  }

}
