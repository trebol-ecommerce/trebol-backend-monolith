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

package org.trebol.api.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.jpa.repositories.SellDetailsRepository;
import org.trebol.jpa.repositories.SellStatusesRepository;
import org.trebol.jpa.services.conversion.ProductsConverterService;
import org.trebol.jpa.services.conversion.SalesConverterService;
import org.trebol.jpa.services.crud.SalesCrudService;
import org.trebol.testing.ProductsTestHelper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.*;

@ExtendWith(MockitoExtension.class)
class SalesProcessServiceImplTest {
  @InjectMocks SalesProcessServiceImpl instance;
  @Mock SalesCrudService crudServiceMock;
  @Mock SalesRepository salesRepositoryMock;
  @Mock SellStatusesRepository sellStatusesRepositoryMock;
  @Mock SellDetailsRepository sellDetailsRepositoryMock;
  @Mock SalesConverterService sellConverterServiceMock;
  @Mock ProductsConverterService productConverterServiceMock;
  final ProductsTestHelper productsHelper = new ProductsTestHelper();

  @BeforeEach
  void beforeEach() {
    productsHelper.resetProducts();
  }

  @Nested
  class MarkAsStarted {

    @Test
    void markAsStarted_SellStatus_IsNotPending_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock));

      assertThrows(BadInputException.class, () -> instance.markAsStarted(sellPojoMock));
    }

    @Test
    void markAsStarted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PENDING);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsStarted(sellPojoMock));
    }

    @Test
    void markAsStarted_ShouldReturn_SellPojo_WithStatusStarted() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PENDING);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_PAYMENT_STARTED, instance.markAsStarted(sellPojoMock).getStatus());
    }
  }

  @Nested
  class MarkAsAborted {

    @Test
    void markAsAborted_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsAborted(sellPojoMock));
    }

    @Test
    void markAsAborted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsAborted(sellPojoMock));
    }

    @Test
    void markAsAborted_ShouldReturn_SellPojo_WithStatusCancelled() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_PAYMENT_CANCELLED, instance.markAsAborted(sellPojoMock).getStatus());
    }
  }

  @Nested
  class MarkAsFailed {

    @Test
    void markAsFailed_SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsFailed(sellPojoMock));
    }

    @Test
    void markAsFailed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsFailed(sellPojoMock));
    }

    @Test
    void markAsFailed_ShouldReturn_SellPojo_WithStatusFailed() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_PAYMENT_FAILED, instance.markAsFailed(sellPojoMock).getStatus());
    }
  }

  @Nested
  class MarkAsPaid {

    @Test
    void markAsPaid__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsPaid(sellPojoMock));
    }

    @Test
    void markAsPaid_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsPaid(sellPojoMock));
    }

    @Test
    void markAsPaid_ShouldReturn_SellPojo_WithStatusUnconfirmed() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_PAID_UNCONFIRMED, instance.markAsPaid(sellPojoMock).getStatus());
    }

    @Test
    void markAsPaid_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAYMENT_STARTED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      SellDetail sellDetailMock = new SellDetail();
      sellDetailMock.setId(1L);
      sellDetailMock.setUnits(11);
      sellDetailMock.setUnitValue(111);
      List<SellDetail> sellDetailsMock = List.of(sellDetailMock);

      ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
      when(sellDetailsRepositoryMock.findBySellId(any())).thenReturn(sellDetailsMock);
      when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

      Collection<SellDetailPojo> actualSellDetailsPojo = instance.markAsPaid(sellPojoMock).getDetails();
      SellDetailPojo actualSellDetailPojo = actualSellDetailsPojo.iterator().next();

      assertEquals(1, actualSellDetailsPojo.size());
      assertEquals(1L, actualSellDetailPojo.getId());
      assertEquals(11, actualSellDetailPojo.getUnits());
      assertEquals(111, actualSellDetailPojo.getUnitValue());
      assertEquals(productPojoMock, actualSellDetailPojo.getProduct());
    }
  }

  @Nested
  class MarkAsConfirmed {

    @Test
    void markAsConfirmed__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsConfirmed(sellPojoMock));
    }

    @Test
    void markAsConfirmed_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsConfirmed(sellPojoMock));
    }

    @Test
    void markAsConfirmed_ShouldReturn_SellPojo_WithStatusConfirmed() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_PAID_CONFIRMED, instance.markAsConfirmed(sellPojoMock).getStatus());
    }

    @Test
    void markAsConfirmed_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      SellDetail sellDetailMock = new SellDetail();
      sellDetailMock.setId(1L);
      sellDetailMock.setUnits(11);
      sellDetailMock.setUnitValue(111);
      List<SellDetail> sellDetailsMock = List.of(sellDetailMock);

      ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
      when(sellDetailsRepositoryMock.findBySellId(any())).thenReturn(sellDetailsMock);
      when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

      Collection<SellDetailPojo> actualSellDetailsPojo = instance.markAsConfirmed(sellPojoMock).getDetails();
      SellDetailPojo actualSellDetailPojo = actualSellDetailsPojo.iterator().next();

      assertEquals(1, actualSellDetailsPojo.size());
      assertEquals(1L, actualSellDetailPojo.getId());
      assertEquals(11, actualSellDetailPojo.getUnits());
      assertEquals(111, actualSellDetailPojo.getUnitValue());
      assertEquals(productPojoMock, actualSellDetailPojo.getProduct());
    }
  }

  @Nested
  class MarkAsRejected {

    @Test
    void markAsRejected__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsRejected(sellPojoMock));
    }

    @Test
    void markAsRejected_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsRejected(sellPojoMock));
    }

    @Test
    void markAsRejected_ShouldReturn_SellPojo_WithStatusRejected() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_REJECTED, instance.markAsRejected(sellPojoMock).getStatus());
    }

    @Test
    void markAsRejected_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_UNCONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      SellDetail sellDetailMock = new SellDetail();
      sellDetailMock.setId(1L);
      sellDetailMock.setUnits(11);
      sellDetailMock.setUnitValue(111);
      List<SellDetail> sellDetailsMock = List.of(sellDetailMock);

      ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
      when(sellDetailsRepositoryMock.findBySellId(any())).thenReturn(sellDetailsMock);
      when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

      Collection<SellDetailPojo> actualSellDetailsPojo = instance.markAsRejected(sellPojoMock).getDetails();
      SellDetailPojo actualSellDetailPojo = actualSellDetailsPojo.iterator().next();

      assertEquals(1, actualSellDetailsPojo.size());
      assertEquals(1L, actualSellDetailPojo.getId());
      assertEquals(11, actualSellDetailPojo.getUnits());
      assertEquals(111, actualSellDetailPojo.getUnitValue());
      assertEquals(productPojoMock, actualSellDetailPojo.getProduct());
    }
  }

  @Nested
  class MarkAsCompleted {

    @Test
    void markAsCompleted__SellStatus_IsNotStarted_BadInputException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName("status");

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException

      assertThrows(BadInputException.class, () -> instance.markAsCompleted(sellPojoMock));
    }

    @Test
    void markAsCompleted_SellStatus_IsNotInRepo_IllegalStateException() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_CONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

      assertThrows(IllegalStateException.class, () -> instance.markAsCompleted(sellPojoMock));
    }

    @Test
    void markAsCompleted_ShouldReturn_SellPojo_WithStatusCompleted() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_CONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException

      assertEquals(SELL_STATUS_COMPLETED, instance.markAsCompleted(sellPojoMock).getStatus());
    }

    @Test
    void markAsCompleted_ShouldReturn_SellPojo_WithCorrectDetails() throws BadInputException {
      // Setup mock objects
      SellPojo sellPojoMock = SellPojo.builder().build();

      SellStatus sellStatusMock = new SellStatus();
      sellStatusMock.setName(SELL_STATUS_PAID_CONFIRMED);

      Sell sellMock = new Sell();
      sellMock.setStatus(sellStatusMock);

      SellDetail sellDetailMock = new SellDetail();
      sellDetailMock.setId(1L);
      sellDetailMock.setUnits(11);
      sellDetailMock.setUnitValue(111);
      List<SellDetail> sellDetailsMock = List.of(sellDetailMock);

      ProductPojo productPojoMock = productsHelper.productPojoAfterCreationWithoutCategory();

      // Stubbing
      when(crudServiceMock.getExisting(any(SellPojo.class))).thenReturn(Optional.of(sellMock)); // fetchExistingOrThrowException
      when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatusMock));
      when(sellConverterServiceMock.convertToPojo(any())).thenReturn(sellPojoMock); // convertOrThrowException
      when(sellDetailsRepositoryMock.findBySellId(any())).thenReturn(sellDetailsMock);
      when(productConverterServiceMock.convertToPojo(any())).thenReturn(productPojoMock);

      Collection<SellDetailPojo> actualSellDetailsPojo = instance.markAsCompleted(sellPojoMock).getDetails();
      SellDetailPojo actualSellDetailPojo = actualSellDetailsPojo.iterator().next();

      assertEquals(1, actualSellDetailsPojo.size());
      assertEquals(1L, actualSellDetailPojo.getId());
      assertEquals(11, actualSellDetailPojo.getUnits());
      assertEquals(111, actualSellDetailPojo.getUnitValue());
      assertEquals(productPojoMock, actualSellDetailPojo.getProduct());
    }
  }
}
