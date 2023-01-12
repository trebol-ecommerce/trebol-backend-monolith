/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "billing_companies")
@NoArgsConstructor
@Getter
@Setter
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

  public BillingCompany(BillingCompany source) {
    this.id = source.id;
    this.idNumber = source.idNumber;
    this.name = source.name;
  }

  public BillingCompany(Long id, String idNumber, String name) {
    this.id = id;
    this.idNumber = idNumber;
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
