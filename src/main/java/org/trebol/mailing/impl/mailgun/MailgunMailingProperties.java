/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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

package org.trebol.mailing.impl.mailgun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Holds configuration properties for using Mailgun as a mail service provider.<br/>
 * A Mailgun account is required to use this.<br/>
 * Read about Mailgun on <a href="https://www.mailgun.com/">their website here</a>.
 */
@Validated
@Component
@ConfigurationProperties(prefix = "trebol.mailing.mailgun")
@Profile("mailgun")
@Data
public class MailgunMailingProperties {
  private String apiKey;
  private String domain;
  private String customerOrderPaymentTemplate;
  private String customerOrderConfirmationTemplate;
  private String customerOrderRejectionTemplate;
  private String customerOrderCompletionTemplate;
  private String ownerOrderConfirmationTemplate;
  private String ownerOrderRejectionTemplate;
  private String ownerOrderCompletionTemplate;
}
