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

/**
 * Wrapper class for data needed to redirect towards payment page
 */
@JsonInclude
public class PaymentRedirectionDetailsPojo {
  private String url;
  private String token;

  public PaymentRedirectionDetailsPojo() { }

  public PaymentRedirectionDetailsPojo(String url, String token) {
    this.url = url;
    this.token = token;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PaymentRedirectionDetailsPojo that = (PaymentRedirectionDetailsPojo) o;
    return Objects.equals(url, that.url) &&
        Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, token);
  }

  @Override
  public String toString() {
    return "PaymentRedirectionDetailsPojo{" +
        "url='" + url + '\'' +
        ", token='" + token + '\'' +
        '}';
  }
}
