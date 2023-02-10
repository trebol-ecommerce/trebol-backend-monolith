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

import kong.unirest.HttpMethod;
import kong.unirest.MockClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.api.models.*;
import org.trebol.mailing.MailingProperties;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.*;
import static org.trebol.mailing.impl.mailgun.MailgunMailingServiceImpl.MAILGUN_HOST;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class MailgunMailingServiceImplTest {
  MailgunMailingServiceImpl instance;
  @Mock MailingProperties integrationPropertiesMock;
  @Mock MailgunMailingProperties mailgunPropertiesMock;
  @Mock ConversionService conversionServiceMock;
  private static final String MOCK_VALID_MAILGUN_JSON_RESPONSE = "{ \"id\": \"ffad4d-3a8abe\" }";
  private static final String MOCK_DATE_FORMAT = "M/d/y z";
  private static final String MOCK_DATE_TIMEZONE = "UTC";
  private static final String MOCK_MAILGUN_DOMAIN = "somedomain.org";
  SellPojo readySell;

  @BeforeEach
  void beforeEach() {
    when(mailgunPropertiesMock.getDomain()).thenReturn(MOCK_MAILGUN_DOMAIN);
    when(mailgunPropertiesMock.getCustomerOrderPaymentTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getCustomerOrderConfirmationTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getCustomerOrderRejectionTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getCustomerOrderCompletionTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getOwnerOrderConfirmationTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getOwnerOrderRejectionTemplate()).thenReturn(ANY);
    when(mailgunPropertiesMock.getOwnerOrderCompletionTemplate()).thenReturn(ANY);
    when(integrationPropertiesMock.getDateFormat()).thenReturn(MOCK_DATE_FORMAT);
    when(integrationPropertiesMock.getDateTimezone()).thenReturn(MOCK_DATE_TIMEZONE);
    when(integrationPropertiesMock.getCustomerOrderPaymentSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getCustomerOrderConfirmationSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getCustomerOrderRejectionSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getCustomerOrderCompletionSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getOwnerOrderConfirmationSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getOwnerOrderCompletionSubject()).thenReturn(ANY);
    when(integrationPropertiesMock.getOwnerOrderRejectionSubject()).thenReturn(ANY);
    instance = new MailgunMailingServiceImpl(integrationPropertiesMock, mailgunPropertiesMock, conversionServiceMock);
    readySell = SellPojo.builder()
      .status(SELL_STATUS_PAID_UNCONFIRMED)
      .buyOrder(1000L)
      .details(List.of(
        SellDetailPojo.builder()
          .description("some product")
          .build(),
        SellDetailPojo.builder()
          .description("some other product")
          .build()
      ))
      .customer(CustomerPojo.builder()
        .person(PersonPojo.builder()
          .firstName(ANY)
          .lastName(ANY)
          .email(ANY)
          .build())
        .build())
      .build();
  }

  @AfterEach
  void afterEach() {
    MockClient.clear();
  }

  @ParameterizedTest
  @ValueSource(strings = {
    SELL_STATUS_PAID_UNCONFIRMED,
    SELL_STATUS_PAID_CONFIRMED,
    SELL_STATUS_REJECTED,
    SELL_STATUS_COMPLETED
  })
  void notifies_statuses_to_customers(String status) {
    ReceiptPojo receipt = new ReceiptPojo();
    when(conversionServiceMock.convert(any(SellPojo.class), eq(ReceiptPojo.class))).thenReturn(receipt);
    MockClient restClient = MockClient.register();
    restClient.expect(HttpMethod.POST, MAILGUN_HOST + MOCK_MAILGUN_DOMAIN + "/messages").thenReturn(MOCK_VALID_MAILGUN_JSON_RESPONSE);
    readySell.setStatus(status);

    assertDoesNotThrow(() -> instance.notifyOrderStatusToClient(readySell));

    verify(conversionServiceMock).convert(readySell, ReceiptPojo.class);
    verify(mailgunPropertiesMock).getDomain();
    verify(mailgunPropertiesMock).getApiKey();
    verify(integrationPropertiesMock).getSenderEmail();
    restClient.verifyAll();
  }

  @ParameterizedTest
  @ValueSource(strings = {
    SELL_STATUS_PENDING,
    SELL_STATUS_PAYMENT_STARTED,
    SELL_STATUS_PAYMENT_CANCELLED,
    SELL_STATUS_PAYMENT_FAILED
  })
  void does_not_notify_some_statuses_to_customers(String status) {
    readySell.setStatus(status);

    assertDoesNotThrow(() -> instance.notifyOrderStatusToClient(readySell));

    verifyNoMoreInteractions(conversionServiceMock);
    verifyNoMoreInteractions(integrationPropertiesMock);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    SELL_STATUS_PAID_CONFIRMED,
    SELL_STATUS_REJECTED,
    SELL_STATUS_COMPLETED
  })
  void notifies_statuses_to_owners(String status) {
    ReceiptPojo receipt = new ReceiptPojo();
    when(conversionServiceMock.convert(any(SellPojo.class), eq(ReceiptPojo.class))).thenReturn(receipt);
    when(integrationPropertiesMock.getOwnerEmail()).thenReturn(ANY);
    MockClient restClient = MockClient.register();
    restClient.expect(HttpMethod.POST, MAILGUN_HOST + MOCK_MAILGUN_DOMAIN + "/messages").thenReturn(MOCK_VALID_MAILGUN_JSON_RESPONSE);
    readySell.setStatus(status);

    assertDoesNotThrow(() -> instance.notifyOrderStatusToOwners(readySell));

    verify(conversionServiceMock).convert(readySell, ReceiptPojo.class);
    verify(mailgunPropertiesMock).getDomain();
    verify(mailgunPropertiesMock).getApiKey();
    verify(integrationPropertiesMock).getSenderEmail();
    verify(integrationPropertiesMock).getOwnerEmail();
    restClient.verifyAll();
  }

  @ParameterizedTest
  @ValueSource(strings = {
    SELL_STATUS_PENDING,
    SELL_STATUS_PAYMENT_STARTED,
    SELL_STATUS_PAYMENT_CANCELLED,
    SELL_STATUS_PAYMENT_FAILED,
    SELL_STATUS_PAID_UNCONFIRMED
  })
  void does_not_notify_some_statuses_to_owners(String status) {
    readySell.setStatus(status);

    assertDoesNotThrow(() -> instance.notifyOrderStatusToOwners(readySell));

    verifyNoMoreInteractions(conversionServiceMock);
    verifyNoMoreInteractions(integrationPropertiesMock);
  }
}
