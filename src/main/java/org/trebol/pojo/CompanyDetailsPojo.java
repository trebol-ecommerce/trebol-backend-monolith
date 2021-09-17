package org.trebol.pojo;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude(ALWAYS)
public class CompanyDetailsPojo {
  private String name;
  private String description;
  private String bannerImageURL;
  private String logoImageURL;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getBannerImageURL() {
    return bannerImageURL;
  }

  public void setBannerImageURL(String bannerImageURL) {
    this.bannerImageURL = bannerImageURL;
  }

  public String getLogoImageURL() {
    return logoImageURL;
  }

  public void setLogoImageURL(String logoImageURL) {
    this.logoImageURL = logoImageURL;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.description);
    hash = 97 * hash + Objects.hashCode(this.bannerImageURL);
    hash = 97 * hash + Objects.hashCode(this.logoImageURL);
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
    final CompanyDetailsPojo other = (CompanyDetailsPojo)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.description, other.description)) {
      return false;
    }
    if (!Objects.equals(this.bannerImageURL, other.bannerImageURL)) {
      return false;
    }
    if (!Objects.equals(this.logoImageURL, other.logoImageURL)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CompanyDetailsPojo{" + "name=" + name + ", description=" + description + ", bannerImageURL=" + bannerImageURL + ", logoImageURL=" + logoImageURL + '}';
  }

}
