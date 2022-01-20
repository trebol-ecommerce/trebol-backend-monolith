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
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

;
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

  public ImagePojo(String code, String filename, String url) {
    this.code = code;
    this.filename = filename;
    this.url = url;
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
