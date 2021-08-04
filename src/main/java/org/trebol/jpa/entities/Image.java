package org.trebol.jpa.entities;

import java.util.Objects;

import org.trebol.jpa.GenericEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Entity
@Table(name = "images")
@NamedQueries({ @NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i") })
public class Image
  implements GenericEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "image_id")
  private Long id;
  @Basic(optional = false)
  @Size(min = 1, max = 100)
  @Column(name = "image_filename")
  private String filename;
  @Basic(optional = false)
  @Size(min = 1, max = 500)
  @Column(name = "image_url")
  private String url;

  public Image() {
  }

  public Image(Long id, String filename, String url) {
    this.id = id;
    this.filename = filename;
    this.url = url;
  }

  @Override
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 43 * hash + Objects.hashCode(this.id);
    hash = 43 * hash + Objects.hashCode(this.filename);
    hash = 43 * hash + Objects.hashCode(this.url);
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
    final Image other = (Image)obj;
    if (!Objects.equals(this.filename, other.filename)) {
      return false;
    }
    if (!Objects.equals(this.url, other.url)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Image{id=" + id +
        ", filename=" + filename +
        ", url=" + url + '}';
  }

}
