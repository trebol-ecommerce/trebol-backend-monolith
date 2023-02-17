/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(
  name = "people",
  indexes = {
    @Index(columnList = "person_id_number"),
    @Index(columnList = "person_first_name"),
    @Index(columnList = "person_last_name"),
    @Index(columnList = "person_email")
  })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Person
  implements Serializable {
  private static final long serialVersionUID = 9L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "person_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 200)
  @Column(name = "person_first_name", nullable = false)
  private String firstName;
  @Size(min = 1, max = 200)
  @Column(name = "person_last_name", nullable = false)
  private String lastName;
  @Size(min = 1, max = 20)
  @Column(name = "person_id_number", nullable = false, unique = true)
  private String idNumber;
  @Size(min = 5, max = 100)
  @Column(name = "person_email", nullable = false)
  private String email;
  @Column(name = "person_phone1", nullable = false)
  @Builder.Default
  private String phone1 = "";
  @Column(name = "person_phone2", nullable = false)
  @Builder.Default
  private String phone2 = "";

  public Person(Person source) {
    this.id = source.id;
    this.firstName = source.firstName;
    this.lastName = source.lastName;
    this.idNumber = source.idNumber;
    this.email = source.email;
    this.phone1 = source.phone1;
    this.phone2 = source.phone2;
  }
}
