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

package org.trebol.integration.mailing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.integration.mailing")
public class MailingProperties {
  private String dateFormat;
  private String dateTimezone;
  private String ownerName;
  private String ownerEmail;
  private String senderEmail;
  private String customerOrderPaymentSubject;
  private String customerOrderConfirmationSubject;
  private String customerOrderRejectionSubject;
  private String customerOrderCompletionSubject;
  private String ownerOrderConfirmationSubject;
  private String ownerOrderRejectionSubject;
  private String ownerOrderCompletionSubject;

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public String getDateTimezone() {
    return dateTimezone;
  }

  public void setDateTimezone(String dateTimezone) {
    this.dateTimezone = dateTimezone;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public String getOwnerEmail() {
    return ownerEmail;
  }

  public void setOwnerEmail(String ownerEmail) {
    this.ownerEmail = ownerEmail;
  }

  public String getSenderEmail() {
    return senderEmail;
  }

  public void setSenderEmail(String senderEmail) {
    this.senderEmail = senderEmail;
  }

  public String getCustomerOrderPaymentSubject() {
    return customerOrderPaymentSubject;
  }

  public void setCustomerOrderPaymentSubject(String customerOrderPaymentSubject) {
    this.customerOrderPaymentSubject = customerOrderPaymentSubject;
  }

  public String getCustomerOrderConfirmationSubject() {
    return customerOrderConfirmationSubject;
  }

  public void setCustomerOrderConfirmationSubject(String customerOrderConfirmationSubject) {
    this.customerOrderConfirmationSubject = customerOrderConfirmationSubject;
  }

  public String getCustomerOrderRejectionSubject() {
    return customerOrderRejectionSubject;
  }

  public void setCustomerOrderRejectionSubject(String customerOrderRejectionSubject) {
    this.customerOrderRejectionSubject = customerOrderRejectionSubject;
  }

  public String getCustomerOrderCompletionSubject() {
    return customerOrderCompletionSubject;
  }

  public void setCustomerOrderCompletionSubject(String customerOrderCompletionSubject) {
    this.customerOrderCompletionSubject = customerOrderCompletionSubject;
  }

  public String getOwnerOrderConfirmationSubject() {
    return ownerOrderConfirmationSubject;
  }

  public void setOwnerOrderConfirmationSubject(String ownerOrderConfirmationSubject) {
    this.ownerOrderConfirmationSubject = ownerOrderConfirmationSubject;
  }

  public String getOwnerOrderRejectionSubject() {
    return ownerOrderRejectionSubject;
  }

  public void setOwnerOrderRejectionSubject(String ownerOrderRejectionSubject) {
    this.ownerOrderRejectionSubject = ownerOrderRejectionSubject;
  }

  public String getOwnerOrderCompletionSubject() {
    return ownerOrderCompletionSubject;
  }

  public void setOwnerOrderCompletionSubject(String ownerOrderCompletionSubject) {
    this.ownerOrderCompletionSubject = ownerOrderCompletionSubject;
  }
}
