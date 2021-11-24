package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "billing_companies")
public class BillingCompany
  implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "billing_company_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 100)
  @Column(name = "billing_company_name", nullable = false, unique = true)
  private String name;
  @Size(min = 1, max = 20)
  @Column(name = "billing_company_id_number", nullable = false, unique = true)
  private String idNumber;

  public BillingCompany() { }

  public BillingCompany(BillingCompany source) {
    this.id = source.id;
    this.name = source.name;
    this.idNumber = source.idNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BillingCompany that = (BillingCompany) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(idNumber, that.idNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, idNumber);
  }

  @Override
  public String toString() {
    return "BillingCompany{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", idNumber='" + idNumber + '\'' +
        '}';
  }
}
