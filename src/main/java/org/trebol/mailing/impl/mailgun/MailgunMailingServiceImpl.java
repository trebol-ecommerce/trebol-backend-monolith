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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.*;
import kong.unirest.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.api.models.*;
import org.trebol.mailing.MailingProperties;
import org.trebol.mailing.MailingService;
import org.trebol.mailing.MailingServiceException;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.trebol.config.Constants.*;

/**
 * Implements Mailgun HTTP API as a mail service provider.<br/>
 * A Mailgun account is required to use this.<br/>
 * Read about Mailgun on <a href="https://www.mailgun.com/">their website here</a>.
 */
@Service
@Profile("mailgun")
public class MailgunMailingServiceImpl
  implements MailingService {
  public static final String MAILGUN_HOST = "https://api.mailgun.net/v3/";
  private static final String CUSTOMER_MAPS_KEY_PREFIX = "customer:";
  private static final String OWNERS_MAPS_KEY_PREFIX = "owners:";
  private final Logger logger = LoggerFactory.getLogger(MailgunMailingServiceImpl.class);
  private final MailingProperties internalMailingIntegrationProperties;
  private final MailgunMailingProperties mailgunProperties;
  private final Map<String, String> orderStatus2MailgunTemplatesMap;
  private final Map<String, String> orderStatus2MailSubjectMap;
  private final ConversionService conversionService;
  private final ObjectMapper mailObjectMapper;
  private final HttpRequestWithBody baseRequestWithBody;

  @Autowired
  public MailgunMailingServiceImpl(
    MailingProperties mailingIntegrationProperties,
    MailgunMailingProperties mailgunProperties,
    ConversionService conversionService
  ) {
    this.internalMailingIntegrationProperties = mailingIntegrationProperties;
    this.mailgunProperties = mailgunProperties;
    this.conversionService = conversionService;
    this.orderStatus2MailgunTemplatesMap = this.makeTemplatesMap();
    this.orderStatus2MailSubjectMap = this.makeSubjectsMap();
    this.mailObjectMapper = this.mailObjectMapper();
    this.baseRequestWithBody = this.prepareBaseApiRequest();
  }

  @Override
  public void notifyOrderStatusToClient(SellPojo sell)
    throws MailingServiceException {
    String mapsKey = CUSTOMER_MAPS_KEY_PREFIX + sell.getStatus();
    if (!orderStatus2MailSubjectMap.containsKey(mapsKey)) {
      return;
    }

    PersonPojo customer = sell.getCustomer().getPerson();
    String customerName = customer.getFirstName() + " " + customer.getLastName();
    String recipient = customerName + " <" + customer.getEmail() + ">";
    String messageSubject = orderStatus2MailSubjectMap.get(mapsKey);
    String mailgunTemplateName = orderStatus2MailgunTemplatesMap.get(mapsKey);
    String fullSubject = messageSubject + " [#" + sell.getBuyOrder() + "]";
    String variables = this.makeMailgunVariablesFrom(sell);
    HttpResponse<JsonNode> request = this.preparePOST(
        recipient,
        fullSubject,
        mailgunTemplateName,
        variables)
      .asJson();
    try {
      if (((String) request.getBody().getObject().get("id")).isBlank()) {
        logger.warn("Mailgun returned the following JSON: {}", request.getBody());
        throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun did not provide an ID for this api");
      }
    } catch (JSONException ex) {
      throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun threw an exception while validating the response", ex);
    }
  }

  @Override
  public void notifyOrderStatusToOwners(SellPojo sell)
    throws MailingServiceException {
    String mapsKey = OWNERS_MAPS_KEY_PREFIX + sell.getStatus();
    if (!orderStatus2MailSubjectMap.containsKey(mapsKey)) {
      return;
    }

    String messageSubject = orderStatus2MailSubjectMap.get(mapsKey);
    String mailgunTemplateName = orderStatus2MailgunTemplatesMap.get(mapsKey);
    String fullSubject = messageSubject + " [#" + sell.getBuyOrder() + "]";
    String variables = this.makeMailgunVariablesFrom(sell);
    HttpResponse<JsonNode> response = this.preparePOST(
        internalMailingIntegrationProperties.getOwnerEmail(),
        fullSubject,
        mailgunTemplateName,
        variables)
      .asJson();
    try {
      if (((String) response.getBody().getObject().get("id")).isBlank()) {
        logger.warn("Mailgun returned the following JSON: {}", response.getBody());
        throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun did not provide an ID for this api");
      }
    } catch (JSONException ex) {
      throw new MailingServiceException("Status of the sent e-mail is unknown, Mailgun threw an exception while validating the response", ex);
    }
  }

  private MultipartBody preparePOST(String to, String subject, String templateName, String templateVariables) {
    return baseRequestWithBody
      .field("from", internalMailingIntegrationProperties.getSenderEmail())
      .field("to", to)
      .field("subject", subject)
      .field("template", templateName)
      .field("h:X-Mailgun-Variables", templateVariables);
  }

  private Map<String, String> makeTemplatesMap() {
    return Map.of(
      CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_PAID_UNCONFIRMED, mailgunProperties.getCustomerOrderPaymentTemplate(),
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
      CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_PAID_UNCONFIRMED, internalMailingIntegrationProperties.getCustomerOrderPaymentSubject(),
      CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, internalMailingIntegrationProperties.getCustomerOrderConfirmationSubject(),
      CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, internalMailingIntegrationProperties.getCustomerOrderRejectionSubject(),
      CUSTOMER_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, internalMailingIntegrationProperties.getCustomerOrderCompletionSubject(),
      OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_PAID_CONFIRMED, internalMailingIntegrationProperties.getOwnerOrderConfirmationSubject(),
      OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_REJECTED, internalMailingIntegrationProperties.getOwnerOrderRejectionSubject(),
      OWNERS_MAPS_KEY_PREFIX + SELL_STATUS_COMPLETED, internalMailingIntegrationProperties.getOwnerOrderCompletionSubject()
    );
  }

  private ObjectMapper mailObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper
      .configOverride(Instant.class)
      .setFormat(JsonFormat.Value
        .forPattern(internalMailingIntegrationProperties.getDateFormat())
        .withTimeZone(TimeZone.getTimeZone(internalMailingIntegrationProperties.getDateTimezone())));
    return objectMapper;
  }

  private HttpRequestWithBody prepareBaseApiRequest() {
    URI targetApiUrl = URI.create(MAILGUN_HOST + this.mailgunProperties.getDomain() + "/messages");
    return Unirest.post(targetApiUrl.toString())
      .basicAuth("api", this.mailgunProperties.getApiKey());
  }

  private String makeMailgunVariablesFrom(SellPojo sell) {
    String variables;
    try {
      ReceiptPojo receipt = this.turnIntoReceipt(sell);
      String transactionJson = mailObjectMapper.writeValueAsString(receipt);
      String customerJson = mailObjectMapper.writeValueAsString(sell.getCustomer());
      variables = "{\"transaction\": " + transactionJson +
        ", \"customer\": " + customerJson + "}";
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Could not stringify transaction object", e);
    }
    return variables;
  }

  private ReceiptPojo turnIntoReceipt(SellPojo sell) {
    ReceiptPojo receipt = conversionService.convert(sell, ReceiptPojo.class);
    if (receipt != null && sell.getDetails() != null) {
      List<ReceiptDetailPojo> receiptDetails = new ArrayList<>();
      for (SellDetailPojo detail : sell.getDetails()) {
        ReceiptDetailPojo convert = conversionService.convert(detail, ReceiptDetailPojo.class);
        receiptDetails.add(convert);
      }
      receipt.setDetails(receiptDetails);
    }
    return receipt;
  }
}
