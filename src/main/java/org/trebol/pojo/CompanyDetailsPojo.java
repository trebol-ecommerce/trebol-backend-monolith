package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CompanyDetailsPojo that = (CompanyDetailsPojo) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(description, that.description) &&
        Objects.equals(bannerImageURL, that.bannerImageURL) &&
        Objects.equals(logoImageURL, that.logoImageURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, bannerImageURL, logoImageURL);
  }

  @Override
  public String toString() {
    return "CompanyDetailsPojo{" +
        "name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", bannerImageURL='" + bannerImageURL + '\'' +
        ", logoImageURL='" + logoImageURL + '\'' +
        '}';
  }
}
