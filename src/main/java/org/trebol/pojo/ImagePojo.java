package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@JsonInclude(NON_NULL)
public class ImagePojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String code;
  @NotBlank
  private String filename;
  @NotBlank
  private String url;

  public ImagePojo() { }

  public ImagePojo(String filename) {
    this.filename = filename;
  }

  public ImagePojo(Long id, String code, String filename, String url) {
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

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
    ImagePojo imagePojo = (ImagePojo) o;
    return Objects.equals(id, imagePojo.id) &&
        Objects.equals(code, imagePojo.code) &&
        Objects.equals(filename, imagePojo.filename) &&
        Objects.equals(url, imagePojo.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, filename, url);
  }

  @Override
  public String toString() {
    return "ImagePojo{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", filename='" + filename + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
