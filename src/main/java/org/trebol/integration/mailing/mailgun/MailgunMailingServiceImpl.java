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

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.trebol.integration.IMailingIntegrationService;
import org.trebol.integration.exceptions.MailingServiceException;
import org.trebol.integration.mailing.MailingProperties;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SellPojo;

import java.util.Map;

import static org.trebol.config.Constants.*;

@Service
@Profile("mailgun")
public class MailgunMailingServiceImpl
  implements IMailingIntegrationService {

  private final Logger logger = LoggerFactory.getLogger(MailgunMailingServiceImpl.class);

  private final MailingProperties internalMailingProperties;
  private final MailgunMailingProperties mailgunProperties;
  private final Map<String, String> orderStatus2MailgunTemplatesMap;
  private final Map<String, String> orderStatus2MailSubjectMap;

  private final static String CUSTOMER_MAPS_KEY_PREFIX = "customer:";
  private final static String OWNERS_MAPS_KEY_PREFIX = "owners:";

  @Autowired
  public MailgunMailingServiceImpl(MailingProperties mailingProperties,
                                   MailgunMailingProperties mailgunProperties) {
    this.internalMailingProperties = mailingProperties;
    this.mailgunProperties = mailgunProperties;
    this.orderStatus2MailgunTemplatesMap = this.makeTemplatesMap();
    this.orderStatus2MailSubjectMap = this.makeSubjectsMap();
  }

  @Override
  public void notifyOrderStatusToClient(SellPojo sell)
      throws MailingServiceException {
    String url = "https://api.mailgun.net/v3/" + mailgunProperties.getDomain() + "/messages";
    PersonPojo customer =  sell.getCustomer().getPerson();
    String customerName = customer.getFirstName() + " " + customer.getLastName();
    String recipient = customerName + " <" + customer.getEmail() + ">";
    String messageSubject = orderStatus2MailSubjectMap.get(CUSTOMER_MAPS_KEY_PREFIX + sell.getStatus());
    String mailgunTemplateName = orderStatus2MailgunTemplatesMap.get(sell.getStatus());

    HttpResponse<JsonNode> request = Unirest.post(url)
			.basicAuth("api", mailgunProperties.getApiKey())
      .field("from", internalMailingProperties.getSenderEmail())
      .field("to", recipient)
      .field("subject", messageSubject + " [#" + sell.getBuyOrder() + "]")
      .field("template", mailgunTemplateName)
      .field("h:X-Mailgun-Variables", "{\"customer\": \"" + customerName + "\"}")
      .asJson();
    try {
      if (((String) request.getBody().getObject().get("id")).isBlank()) {
        logger.warn("Mailgun returned the following JSON: {}", request.getBody());
        throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun did not provide an ID for this operation");
      }
    } catch (JSONException ex) {
      throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun threw an exception while validating the response", ex);
    }
  }

  @Override
  public void notifyOrderStatusToOwners(SellPojo sell)
      throws MailingServiceException {
    String url = "https://api.mailgun.net/v3/" + mailgunProperties.getDomain() + "/messages";
    String messageSubject = orderStatus2MailSubjectMap.get(OWNERS_MAPS_KEY_PREFIX + sell.getStatus());
    String mailgunTemplateName = orderStatus2MailgunTemplatesMap.get(sell.getStatus());

    HttpResponse<JsonNode> request = Unirest.post(url)
      .basicAuth("api", mailgunProperties.getApiKey())
      .field("from", internalMailingProperties.getSenderEmail())
      .field("to", internalMailingProperties.getOwnerEmail())
      .field("subject", messageSubject + " [#" + sell.getBuyOrder() + "]")
      .field("template", mailgunTemplateName)
      .field("h:X-Mailgun-Variables", "{\"customer\": \"" + internalMailingProperties.getOwnerName() + "\"}")
      .asJson();
    try {
      if (((String) request.getBody().getObject().get("id")).isBlank()) {
        logger.warn("Mailgun returned the following JSON: {}", request.getBody());
        throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun did not provide an ID for this operation");
      }
    } catch (JSONException ex) {
      throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun threw an exception while validating the response", ex);
    }
  }

  private Map<String, String> makeTemplatesMap() {
    return Map.of(
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, mailgunProperties.getCustomerOrderConfirmationTemplate(),
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, mailgunProperties.getCustomerOrderRejectionTemplate(),
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, mailgunProperties.getCustomerOrderCompletionTemplate(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, mailgunProperties.getOwnerOrderConfirmationTemplate(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, mailgunProperties.getOwnerOrderRejectionTemplate(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, mailgunProperties.getOwnerOrderCompletionTemplate()
    );
  }

  private Map<String, String> makeSubjectsMap() {
    return Map.of(
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, internalMailingProperties.getCustomerOrderConfirmationSubject(),
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, internalMailingProperties.getCustomerOrderRejectionSubject(),
        CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, internalMailingProperties.getCustomerOrderCompletionSubject(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, internalMailingProperties.getOwnerOrderConfirmationSubject(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, internalMailingProperties.getOwnerOrderRejectionSubject(),
        OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, internalMailingProperties.getOwnerOrderCompletionSubject()
    );
  }
}
