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

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

;
@JsonInclude(NON_NULL)
public class PersonPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String idNumber;
  @NotBlank
  private String email;
  @Pattern(regexp = "^(((\\(\\+?[0-9]{3}\\))|(\\+?[0-9]{3})) ?)?[0-9]{3,4}[ -]?[0-9]{4}$")
  private String phone1;
  @Pattern(regexp = "^(((\\(\\+?[0-9]{3}\\))|(\\+?[0-9]{3})) ?)?[0-9]{3,4}[ -]?[0-9]{4}$")
  private String phone2;

  public PersonPojo() { }

  public PersonPojo(String idNumber) {
    this.idNumber = idNumber;
  }

  public PersonPojo(String firstName, String lastName, String idNumber, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.idNumber = idNumber;
    this.email = email;
  }

  public PersonPojo(Long id,
                    String firstName,
                    String lastName,
                    String idNumber,
                    String email,
                    String phone1,
                    String phone2) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.idNumber = idNumber;
    this.email = email;
    this.phone1 = phone1;
    this.phone2 = phone2;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone1() {
    return phone1;
  }

  public void setPhone1(String phone1) {
    this.phone1 = phone1;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonPojo that = (PersonPojo) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(firstName, that.firstName) &&
        Objects.equals(lastName, that.lastName) &&
        Objects.equals(idNumber, that.idNumber) &&
        Objects.equals(email, that.email) &&
        Objects.equals(phone1, that.phone1) &&
        Objects.equals(phone2, that.phone2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, idNumber, email, phone1, phone2);
  }

  @Override
  public String toString() {
    return "PersonPojo{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", idNumber='" + idNumber + '\'' +
        ", email='" + email + '\'' +
        ", phone1='" + phone1 + '\'' +
        ", phone2='" + phone2 + '\'' +
        '}';
  }
}
