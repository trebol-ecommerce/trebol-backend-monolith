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

package org.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.security")
public class SecurityProperties {

  @NotBlank
  private String jwtSecretKey;
  @PositiveOrZero
  private int jwtExpirationAfterMinutes;
  @PositiveOrZero
  private int jwtExpirationAfterHours;
  @PositiveOrZero
  private int jwtExpirationAfterDays;
  @Min(6)
  private int bcryptEncoderStrength;
  private boolean guestUserEnabled;
  private String guestUserName;
  private boolean accountProtectionEnabled;
  @Min(1)
  private int protectedAccountId; 

  public String getJwtSecretKey() {
    return jwtSecretKey;
  }

  public void setJwtSecretKey(String jwtSecretKey) {
    this.jwtSecretKey = jwtSecretKey;
  }

  public int getJwtExpirationAfterDays() {
    return jwtExpirationAfterDays;
  }

  public void setJwtExpirationAfterDays(int jwtExpirationAfterDays) {
    this.jwtExpirationAfterDays = jwtExpirationAfterDays;
  }

  public int getJwtExpirationAfterMinutes() {
    return jwtExpirationAfterMinutes;
  }

  public void setJwtExpirationAfterMinutes(int jwtExpirationAfterMinutes) {
    this.jwtExpirationAfterMinutes = jwtExpirationAfterMinutes;
  }

  public int getJwtExpirationAfterHours() {
    return jwtExpirationAfterHours;
  }

  public void setJwtExpirationAfterHours(int jwtExpirationAfterHours) {
    this.jwtExpirationAfterHours = jwtExpirationAfterHours;
  }

  public int getBcryptEncoderStrength() {
    return bcryptEncoderStrength;
  }

  public void setBcryptEncoderStrength(int bcryptEncoderStrength) {
    this.bcryptEncoderStrength = bcryptEncoderStrength;
  }

  public boolean isGuestUserEnabled() {
    return guestUserEnabled;
  }

  public void setGuestUserEnabled(boolean guestUserEnabled) {
    this.guestUserEnabled = guestUserEnabled;
  }

  public String getGuestUserName() {
    return guestUserName;
  }

  public void setGuestUserName(String guestUserName) {
    this.guestUserName = guestUserName;
  }
  
  public boolean isAccountProtectionEnabled() {
	  return accountProtectionEnabled;
  }
  
  public void setAccountProtectionEnabled(boolean accountProtectionEnabled) {
	  this.accountProtectionEnabled = accountProtectionEnabled;
  }
  
  public int getProtectedAccountId() {
	  return protectedAccountId;
  }
  
  public void setProtectedAccountId(int protectedAccountId) {
	  this.protectedAccountId = protectedAccountId;
  }

}