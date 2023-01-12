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

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "sell_statuses")
public class SellStatus
  implements Serializable {
  private static final long serialVersionUID = 16L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sell_status_id", nullable = false)
  private Long id;
  @Column(name = "sell_status_code", nullable = false, unique = true)
  private Integer code;
  @Size(min = 1, max = 100)
  @Column(name = "sell_status_name", nullable = false, unique = true)
  private String name;

  public SellStatus() {
  }

  public SellStatus(SellStatus source) {
    this.id = source.id;
    this.code = source.code;
    this.name = source.name;
  }

  public SellStatus(Long id, Integer code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
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
    SellStatus that = (SellStatus) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(code, that.code) &&
      Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name);
  }

  @Override
  public String toString() {
    return "SellStatus{" +
      "id=" + id +
      ", code=" + code +
      ", name='" + name + '\'' +
      '}';
  }
}
