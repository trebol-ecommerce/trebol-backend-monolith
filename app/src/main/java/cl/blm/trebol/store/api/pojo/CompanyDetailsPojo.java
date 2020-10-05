package cl.blm.trebol.store.api.pojo;

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

}
