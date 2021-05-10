package org.trebol.api.pojo;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class ImagePojo {
  @JsonInclude
  @NotNull
  private String filename;
  @JsonInclude
  @NotNull
  private String url;

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
  public int hashCode() {
    int hash = 7;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ImagePojo other = (ImagePojo)obj;
    if (!Objects.equals(this.filename, other.filename)) {
      return false;
    }
    if (!Objects.equals(this.url, other.url)) {
      return false;
    }
    return true;
  }
}
