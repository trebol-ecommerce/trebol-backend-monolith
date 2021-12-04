package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude
public class BillingCompanyPojo {
  private String idNumber;
  private String name;

  public BillingCompanyPojo() {
  }

  public BillingCompanyPojo(String idNumber) {
    this.idNumber = idNumber;
  }

  public BillingCompanyPojo(String idNumber, String name) {
    this.idNumber = idNumber;
    this.name = name;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BillingCompanyPojo that = (BillingCompanyPojo) o;
    return Objects.equals(idNumber, that.idNumber) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idNumber, name);
  }

  @Override
  public String toString() {
    return "BillingCompanyPojo{" +
        "idNumber='" + idNumber + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
