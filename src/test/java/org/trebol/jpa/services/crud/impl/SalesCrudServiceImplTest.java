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
import org.trebol.api.models.AddressPojo;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.AddressesRepository;
import org.trebol.jpa.repositories.BillingTypesRepository;
import org.trebol.jpa.repositories.ProductsRepository;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.jpa.services.conversion.*;
import org.trebol.jpa.services.crud.BillingCompaniesCrudService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.jpa.services.crud.ShippersCrudService;
import org.trebol.jpa.services.patch.SalesPatchService;
import org.trebol.testing.CustomersTestHelper;
import org.trebol.testing.ProductsTestHelper;
import org.trebol.testing.SalesTestHelper;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class SalesCrudServiceImplTest {
  @InjectMocks SalesCrudServiceImpl instance;
  @Mock SalesRepository salesRepositoryMock;
  @Mock ProductsRepository productsRepositoryMock;
  @Mock SalesConverterService salesConverterMock;
  @Mock SalesPatchService salesPatchServiceMock;
  @Mock ProductsConverterService productsConverterServiceMock; // TODO write an unit test that needs this mock
  @Mock CustomersCrudService customersCrudServiceMock;
  @Mock CustomersConverterService customersConverterServiceMock;
  @Mock BillingTypesRepository billingTypesRepositoryMock;
  @Mock BillingCompaniesCrudService billingCompaniesCrudServiceMock;
  @Mock BillingCompaniesConverterService billingCompaniesConverterServiceMock;
  @Mock AddressesRepository addressesRepositoryMock;
  @Mock ShippersCrudService shippersCrudServiceMock;
  @Mock AddressesConverterService addressesConverterServiceMock;
  final ProductsTestHelper productsHelper = new ProductsTestHelper();
  final SalesTestHelper salesHelper = new SalesTestHelper();
  final CustomersTestHelper customersHelper = new CustomersTestHelper();

  @BeforeEach
  void beforeEach() {
    productsHelper.resetProducts();
    salesHelper.resetSales();
    customersHelper.resetCustomers();
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
  void finds_using_predicates() throws EntityNotFoundException {
    SellPojo expectedResult = SellPojo.builder()
      .buyOrder(1L)
      .details(List.of()) // omit conversion cascading
      .build();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(salesHelper.sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.readOne(new BooleanBuilder());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void does_not_create_sales_with_incomplete_data() throws BadInputException {
    Customer customer = customersHelper.customerEntityAfterCreation();
    when(salesConverterMock.convertToNewEntity(any(SellPojo.class))).thenReturn(new Sell());
    when(customersCrudServiceMock.getExisting(nullable(CustomerPojo.class))).thenReturn(Optional.of(customer));
    when(billingTypesRepositoryMock.findByName(nullable(String.class))).thenReturn(Optional.of(new BillingType()));
    when(addressesConverterServiceMock.convertToNewEntity(any(AddressPojo.class))).thenReturn(new Address());

    List.of(
      SellPojo.builder().build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .billingType(ANY)
        .build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .billingType(ANY)
        .billingAddress(AddressPojo.builder().build())
        .build()
    ).forEach((sellPojo) -> assertThrows(
      NullPointerException.class,
      () -> instance.create(sellPojo)
    ));
    verifyNoInteractions(salesRepositoryMock);

    SellPojo valid = SellPojo.builder()
      .customer(customersHelper.customerPojoBeforeCreation())
      .billingType(ANY)
      .billingAddress(AddressPojo.builder().build())
      .details(List.of())
      .build();
    SellPojo expectedResult = SellPojo.builder()
      .date(Instant.now())
      .build();
    when(salesConverterMock.convertToPojo(nullable(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.create(valid);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_sell()
    throws BadInputException, EntityExistsException {
    SellPojo input = SellPojo.builder()
      .customer(customersHelper.customerPojoBeforeCreation())
      .billingType(ANY)
      .billingAddress(AddressPojo.builder().build())
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(productsHelper.productPojoBeforeCreationWithoutCategory())
          .build()
      ))
      .build();
    SellPojo expectedResult = salesHelper.sellPojoAfterCreation();
    when(salesConverterMock.convertToNewEntity(any(SellPojo.class))).thenReturn(new Sell());
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.empty());
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(new BillingType()));
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(new Product()));
    when(salesConverterMock.convertToPojo(nullable(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.create(input);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void updates_sell()
    throws BadInputException, EntityNotFoundException {
    SellPojo input = salesHelper.sellPojoAfterCreation();
    Instant updatedDate = Instant.now().minus(Duration.ofHours(1L));
    input.setDate(updatedDate);
    Sell internalResult = new Sell(salesHelper.sellEntityAfterCreation());
    internalResult.setDate(updatedDate);
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(salesHelper.sellEntityAfterCreation()));
    when(salesPatchServiceMock.patchExistingEntity(any(SellPojo.class), any(Sell.class))).thenReturn(internalResult);
    when(salesRepositoryMock.saveAndFlush(any(Sell.class))).thenReturn(internalResult);
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(input);

    SellPojo result = instance.update(input, new BooleanBuilder());

    assertNotNull(result);
    assertEquals(input, result);
  }

  @Test
  void returns_same_when_no_update_is_made()
    throws BadInputException, EntityNotFoundException {
    SellPojo input = salesHelper.sellPojoAfterCreation();
    Sell matchingEntity = salesHelper.sellEntityAfterCreation();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(matchingEntity));
    when(salesPatchServiceMock.patchExistingEntity(any(SellPojo.class), any(Sell.class))).thenReturn(matchingEntity);

    SellPojo result = instance.update(input, new BooleanBuilder());

    verify(salesPatchServiceMock).patchExistingEntity(input, matchingEntity);
    assertEquals(input, result);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    BooleanBuilder anyPredicate = new BooleanBuilder();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> instance.readOne(anyPredicate));
  }
}
