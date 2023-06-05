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

package org.trebol.jpa.services.crud.impl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.SellPojo;
import org.trebol.config.ApiProperties;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.SalesConverterService;
import org.trebol.testing.SalesTestHelper;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesCrudServiceImplTest {
  @InjectMocks SalesCrudServiceImpl instance;
  @Mock SalesRepository salesRepositoryMock;
  @Mock SalesConverterService salesConverterMock;
  @Mock AddressesConverterService addressesConverterServiceMock; // TODO verify usage of this mock when reading one
  @Mock ApiProperties apiProperties; // TODO verify usage of this mock when doing a partial update
  final SalesTestHelper salesHelper = new SalesTestHelper();

  @BeforeEach
  void beforeEach() {
    salesHelper.resetSales();
  }

  @Test
  void finds_by_example_using_buy_order() {
    SellPojo input = salesHelper.sellPojoForFetch();
    Sell expectedResult = salesHelper.sellEntityAfterCreation();
    when(salesRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedResult));

    Optional<Sell> match = instance.getExisting(input);

    verify(salesRepositoryMock).findById(input.getBuyOrder());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void finds_using_predicate() throws EntityNotFoundException {
    SellPojo expectedResult = SellPojo.builder()
      .buyOrder(1L)
      .details(List.of()) // TODO put some details here to provide coverage to their conversion as well
      .build();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(salesHelper.sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.readOne(new BooleanBuilder());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    BooleanBuilder anyPredicate = new BooleanBuilder();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> instance.readOne(anyPredicate));
  }
}
