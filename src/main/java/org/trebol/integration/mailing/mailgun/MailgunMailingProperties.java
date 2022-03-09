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

package org.trebol.integration.mailing.mailgun;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "trebol.integration.mailing.mailgun")
@Profile("mailgun")
public class MailgunMailingProperties {
  private String apiKey;
  private String domain;
  private String customerOrderConfirmationTemplate;
  private String customerOrderRejectionTemplate;
  private String customerOrderCompletionTemplate;
  private String ownerOrderConfirmationTemplate;
  private String ownerOrderRejectionTemplate;
  private String ownerOrderCompletionTemplate;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getCustomerOrderConfirmationTemplate() {
    return customerOrderConfirmationTemplate;
  }

  public void setCustomerOrderConfirmationTemplate(String customerOrderConfirmationTemplate) {
    this.customerOrderConfirmationTemplate = customerOrderConfirmationTemplate;
  }

  public String getCustomerOrderRejectionTemplate() {
    return customerOrderRejectionTemplate;
  }

  public void setCustomerOrderRejectionTemplate(String customerOrderRejectionTemplate) {
    this.customerOrderRejectionTemplate = customerOrderRejectionTemplate;
  }

  public String getCustomerOrderCompletionTemplate() {
    return customerOrderCompletionTemplate;
  }

  public void setCustomerOrderCompletionTemplate(String customerOrderCompletionTemplate) {
    this.customerOrderCompletionTemplate = customerOrderCompletionTemplate;
  }

  public String getOwnerOrderConfirmationTemplate() {
    return ownerOrderConfirmationTemplate;
  }

  public void setOwnerOrderConfirmationTemplate(String ownerOrderConfirmationTemplate) {
    this.ownerOrderConfirmationTemplate = ownerOrderConfirmationTemplate;
  }

  public String getOwnerOrderRejectionTemplate() {
    return ownerOrderRejectionTemplate;
  }

  public void setOwnerOrderRejectionTemplate(String ownerOrderRejectionTemplate) {
    this.ownerOrderRejectionTemplate = ownerOrderRejectionTemplate;
  }

  public String getOwnerOrderCompletionTemplate() {
    return ownerOrderCompletionTemplate;
  }

  public void setOwnerOrderCompletionTemplate(String ownerOrderCompletionTemplate) {
    this.ownerOrderCompletionTemplate = ownerOrderCompletionTemplate;
  }
}
