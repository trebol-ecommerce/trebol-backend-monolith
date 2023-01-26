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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "images")
@NoArgsConstructor
@Getter
@Setter
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
