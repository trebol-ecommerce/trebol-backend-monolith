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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-mailgun.empty.properties")
@EnableConfigurationProperties(MailgunMailingProperties.class)
class MailgunMailingPropertiesTest {
  @Autowired MailgunMailingProperties integrationProperties;

  @Test
  void sanity_check() {
    assertNotNull(integrationProperties);
    List.of(
      integrationProperties.getApiKey(),
      integrationProperties.getDomain(),
      integrationProperties.getCustomerOrderPaymentTemplate(),
      integrationProperties.getCustomerOrderConfirmationTemplate(),
      integrationProperties.getCustomerOrderRejectionTemplate(),
      integrationProperties.getCustomerOrderCompletionTemplate(),
      integrationProperties.getOwnerOrderConfirmationTemplate(),
      integrationProperties.getOwnerOrderRejectionTemplate(),
      integrationProperties.getOwnerOrderCompletionTemplate()
    ).forEach(property -> {
      assertNotNull(property);
      assertTrue(property.isBlank());
    });
  }
}
