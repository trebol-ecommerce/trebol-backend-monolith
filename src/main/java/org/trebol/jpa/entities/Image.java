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
@Table(name = "images")
public class Image
  implements Serializable {

  private static final long serialVersionUID = 5L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "image_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 50)
  @Column(name = "image_code", nullable = false, unique = true)
  private String code;
  @Size(min = 1, max = 100)
  @Column(name = "image_filename", nullable = false, unique = true)
  private String filename;
  @Size(min = 1, max = 500)
  @Column(name = "image_url", nullable = false, unique = true)
  private String url;

  public Image() { }

  public Image(Image source) {
    this.id = source.id;
    this.code = source.code;
    this.filename = source.filename;
    this.url = source.url;
  }

  public Image(String code, String filename, String url) {
    this.code = code;
    this.filename = filename;
    this.url = url;
  }

  public Image(Long id, String code, String filename, String url) {
    this.id = id;
    this.code = code;
    this.filename = filename;
    this.url = url;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Image image = (Image) o;
    return Objects.equals(id, image.id) &&
        Objects.equals(code, image.code) &&
        Objects.equals(filename, image.filename) &&
        Objects.equals(url, image.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, filename, url);
  }

  @Override
  public String toString() {
    return "Image{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", filename='" + filename + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
