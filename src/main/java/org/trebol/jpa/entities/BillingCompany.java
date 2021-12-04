package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

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
  @Size(min = 1, max = 20)
  @Column(name = "billing_company_id_number", nullable = false, unique = true)
  private String idNumber;
  @Size(min = 1, max = 100)
  @Column(name = "billing_company_name", nullable = false, unique = true)
  private String name;

  public BillingCompany() { }

  public BillingCompany(BillingCompany source) {
    this.id = source.id;
    this.idNumber = source.idNumber;
    this.name = source.name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    BillingCompany that = (BillingCompany) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(idNumber, that.idNumber) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idNumber, name);
  }

  @Override
  public String toString() {
    return "BillingCompany{" +
        "id=" + id +
        ", idNumber='" + idNumber + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
