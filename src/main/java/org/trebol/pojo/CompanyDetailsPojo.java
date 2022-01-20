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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

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
