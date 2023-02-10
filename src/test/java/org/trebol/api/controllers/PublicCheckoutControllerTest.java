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

package org.trebol.api.controllers;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.trebol.api.models.PaymentRedirectionDetailsPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.api.services.CheckoutService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.jpa.services.predicates.SalesPredicateService;
import org.trebol.mailing.MailingService;
import org.trebol.mailing.MailingServiceException;
import org.trebol.payment.PaymentServiceException;
import org.trebol.testing.SalesTestHelper;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.trebol.config.Constants.WEBPAY_ABORTION_TOKEN_HEADER_NAME;
import static org.trebol.config.Constants.WEBPAY_SUCCESS_TOKEN_HEADER_NAME;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class PublicCheckoutControllerTest {
  @InjectMocks PublicCheckoutController instance;
  @Mock CheckoutService serviceMock;
  @Mock SalesCrudService salesCrudServiceMock;
  @Mock SalesPredicateService salesPredicateServiceMock;
  @Mock MailingService mailingServiceMock;
  final SalesTestHelper salesHelper = new SalesTestHelper();
  private final static Map<String, String> HEADERS_MAP_WITH_SUCCESS_TOKEN = Map.of(WEBPAY_SUCCESS_TOKEN_HEADER_NAME, ANY);
  private final static Map<String, String> HEADERS_MAP_WITH_ABORTED_TOKEN = Map.of(WEBPAY_ABORTION_TOKEN_HEADER_NAME, ANY);

  @Test
  void creates_redirection_data_from_cart_data() throws BadInputException, PaymentServiceException {
    PaymentRedirectionDetailsPojo expectedResult = PaymentRedirectionDetailsPojo.builder()
      .token(ANY)
      .url(ANY)
      .build();
    SellPojo input = salesHelper.sellPojoBeforeCreation();
    when(serviceMock.requestTransactionStart(nullable(SellPojo.class))).thenReturn(expectedResult);
    PaymentRedirectionDetailsPojo result = instance.submitCart(input);
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void redirects_to_success_page() throws BadInputException, MailingServiceException, PaymentServiceException {
    mailingServiceMock = null;
    URI successPageUri = URI.create(ANY);
    when(serviceMock.generateResultPageUrl(anyString())).thenReturn(successPageUri);
    ResponseEntity<Void> response = instance.validateSuccesfulTransaction(HEADERS_MAP_WITH_SUCCESS_TOKEN);
    assertNotNull(response);
    assertEquals(SEE_OTHER, response.getStatusCode());
    assertEquals(successPageUri, response.getHeaders().getLocation());
  }

  @Test
  void redirects_to_failure_page() throws BadInputException, PaymentServiceException {
    URI failurePageUri = URI.create(ANY);
    when(serviceMock.generateResultPageUrl(anyString())).thenReturn(failurePageUri);
    ResponseEntity<Void> response = instance.validateAbortedTransaction(HEADERS_MAP_WITH_ABORTED_TOKEN);
    assertNotNull(response);
    assertEquals(SEE_OTHER, response.getStatusCode());
    assertEquals(failurePageUri, response.getHeaders().getLocation());
  }

  @Test
  void reads_result_of_transaction() {
    SellPojo expectedResult = SellPojo.builder().build();
    when(salesCrudServiceMock.readOne(nullable(Predicate.class))).thenReturn(expectedResult);
    SellPojo result = instance.getTransactionResultFor(ANY);
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }
}
