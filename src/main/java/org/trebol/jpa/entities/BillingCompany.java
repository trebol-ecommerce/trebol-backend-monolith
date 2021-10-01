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
@Table(
  name = "billing_companies",
  uniqueConstraints = @UniqueConstraint(columnNames = {"billing_company_id_number"}))
public class BillingCompany
  implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "billing_company_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 100)
  @Column(name = "billing_company_name", nullable = false)
  private String name;
  @Size(min = 1, max = 20)
  @Column(name = "billing_company_id_number", nullable = false)
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
  public int hashCode() {
    int hash = 3;
    hash = 31 * hash + Objects.hashCode(this.id);
    hash = 31 * hash + Objects.hashCode(this.name);
    hash = 31 * hash + Objects.hashCode(this.idNumber);
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
    final BillingCompany other = (BillingCompany)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.idNumber, other.idNumber)) {
      return false;
    }
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {
    return "BillingCompany{id=" + id +
        ", name=" + name +
        ", idNumber=" + idNumber + '}';
  }

}
