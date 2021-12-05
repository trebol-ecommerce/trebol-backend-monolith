package org.trebol.jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "sales_statuses")
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

  public SellStatus() { }

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
