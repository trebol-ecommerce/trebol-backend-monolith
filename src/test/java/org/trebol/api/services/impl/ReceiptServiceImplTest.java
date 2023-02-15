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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.api.models.ReceiptDetailPojo;
import org.trebol.api.models.ReceiptPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.SalesRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceImplTest {
  @InjectMocks ReceiptServiceImpl instance;
  @Mock SalesRepository salesRepositoryMock;
  @Mock ConversionService conversionServiceMock;
  Sell sellMock;

  @BeforeEach
  void beforeEach() {
    sellMock = Sell.builder()
      .details(List.of(
        SellDetail.builder()
          .product(Product.builder()
            .name(ANY)
            .barcode(ANY)
            .build())
          .units(1)
          .build()))
      .status(SellStatus.builder()
        .name(ANY).build())
      .build();
  }

  @Test
  @DisplayName("When Sale not found by token, throw EntityNotFoundException")
  void fetchReceiptByTransactionToken_SaleNotFound_EntityNotFoundException() {
    when(salesRepositoryMock.findByTransactionToken(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> instance.fetchReceiptByTransactionToken(ANY));
  }

  @Test
  @DisplayName("When Sale found by token, no exception")
  void fetchReceiptByTransactionToken_SaleFound_NoException() {
    ReceiptPojo receiptPojoMock = new ReceiptPojo();
    ReceiptDetailPojo receiptDetailPojoMock = new ReceiptDetailPojo();
    when(salesRepositoryMock.findByTransactionToken(anyString())).thenReturn(Optional.of(sellMock));
    when(conversionServiceMock.convert(any(Sell.class), eq(ReceiptPojo.class))).thenReturn(receiptPojoMock);
    when(conversionServiceMock.convert(any(SellDetail.class), eq(ReceiptDetailPojo.class))).thenReturn(receiptDetailPojoMock);

    assertDoesNotThrow(() -> instance.fetchReceiptByTransactionToken("token"));
  }

  @Test
  @DisplayName("When Sale found by token, should return the correct ReceiptPojo")
  void fetchReceiptByTransactionToken_ReturnsReceiptPojo() {
    ReceiptPojo receiptPojoMock = new ReceiptPojo();
    ReceiptDetailPojo receiptDetailPojoMock = new ReceiptDetailPojo();
    when(salesRepositoryMock.findByTransactionToken(anyString())).thenReturn(Optional.of(sellMock));
    when(conversionServiceMock.convert(any(Sell.class), eq(ReceiptPojo.class))).thenReturn(receiptPojoMock);
    when(conversionServiceMock.convert(any(SellDetail.class), eq(ReceiptDetailPojo.class))).thenReturn(receiptDetailPojoMock);

    ReceiptPojo actualReceiptPojo = instance.fetchReceiptByTransactionToken(ANY);

    assertEquals(receiptPojoMock, actualReceiptPojo);
  }

}
