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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.AddressPojo;
import org.trebol.api.models.BillingCompanyPojo;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.models.SellDetailPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Address;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.AddressesRepository;
import org.trebol.jpa.repositories.BillingTypesRepository;
import org.trebol.jpa.repositories.ProductsRepository;
import org.trebol.jpa.repositories.ShippersRepository;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.conversion.ProductsConverterService;
import org.trebol.jpa.services.conversion.SalespeopleConverterService;
import org.trebol.jpa.services.crud.BillingCompaniesCrudService;
import org.trebol.jpa.services.crud.CustomersCrudService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.jpa.services.conversion.impl.SalesConverterServiceImpl.UNEXISTING_BILLING_TYPE;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class SalesConverterServiceImplTest {
  @InjectMocks SalesConverterServiceImpl instance;
  @Mock CustomersCrudService customersCrudServiceMock;
  @Mock CustomersConverterService customersConverterServiceMock;
  @Mock BillingTypesRepository billingTypesRepositoryMock;
  @Mock BillingCompaniesCrudService billingCompaniesCrudServiceMock;
  @Mock BillingCompaniesConverterService billingCompaniesConverterServiceMock;
  @Mock SalespeopleConverterService salespeopleConverterServiceMock;
  @Mock ProductsConverterService productConverterServiceMock;
  @Mock ProductsRepository productsRepositoryMock;
  @Mock ShippersRepository shippersRepositoryMock;
  @Mock AddressesRepository addressesRepositoryMock;
  @Mock AddressesConverterService addressesConverterServiceMock;
  private static final Instant SOME_INSTANT = Instant.now();
  private static final CustomerPojo SOME_CUSTOMER = CustomerPojo.builder().build();
  private static final BillingCompanyPojo SOME_BILLING_COMPANY = BillingCompanyPojo.builder().build();
  private static final AddressPojo SOME_ADDRESS;
  private static final Customer SOME_CUSTOMER_ENTITY = Customer.builder().build();
  private static final BillingType SOME_BILLING_TYPE_ENTITY = BillingType.builder().build();
  private static final BillingCompany SOME_BILLING_COMPANY_ENTITY = BillingCompany.builder().build();
  private static final Address SOME_ADDRESS_ENTITY = Address.builder().build();
  private static final Product SOME_PRODUCT_ENTITY = Product.builder().build();

  static {
    SOME_ADDRESS = AddressPojo.builder()
        .firstLine(ANY)
        .secondLine(ANY)
        .city(ANY)
        .municipality(ANY)
        .postalCode(ANY)
        .notes(ANY)
        .build();
  }

  @Test
  void converts_to_new_entity() throws BadInputException {
    SellPojo input = SellPojo.builder()
      .date(SOME_INSTANT)
      .customer(SOME_CUSTOMER)
      .billingType(ANY)
      .billingAddress(SOME_ADDRESS)
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(ProductPojo.builder()
            .barcode(ANY)
            .build())
          .build()
      ))
      .build();
    Customer existingCustomer = Customer.builder().build();
    BillingType existingBillingType = BillingType.builder().build();
    Product existingProduct = Product.builder().build();
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(existingCustomer));
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingBillingType));
    when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.empty());
    when(addressesConverterServiceMock.convertToNewEntity(any(AddressPojo.class))).thenReturn(null);
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(existingProduct));
    Sell result = instance.convertToNewEntity(input);
    assertNull(result.getBillingAddress());
  }

  @Test
  void converts_to_new_entity_with_new_billing_address() throws BadInputException {
    SellPojo input = SellPojo.builder()
      .date(SOME_INSTANT)
      .customer(SOME_CUSTOMER)
      .billingType(ANY)
      .billingAddress(SOME_ADDRESS)
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(ProductPojo.builder()
            .barcode(ANY)
            .build())
          .build()
      ))
      .build();
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(SOME_CUSTOMER_ENTITY));
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
    when(addressesConverterServiceMock.convertToNewEntity(any(AddressPojo.class))).thenReturn(SOME_ADDRESS_ENTITY);
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(SOME_PRODUCT_ENTITY));
    Sell result = instance.convertToNewEntity(input);
    assertEquals(SOME_ADDRESS_ENTITY, result.getBillingAddress());
    verify(addressesRepositoryMock).findByFields(ANY, ANY, ANY, ANY, ANY, ANY);
    verify(addressesConverterServiceMock).convertToNewEntity(input.getBillingAddress());
  }

  @Test
  void converts_to_new_entity_with_existing_billing_address() throws BadInputException {
    SellPojo input = SellPojo.builder()
      .date(SOME_INSTANT)
      .customer(SOME_CUSTOMER)
      .billingType(ANY)
      .billingAddress(SOME_ADDRESS)
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(ProductPojo.builder()
            .barcode(ANY)
            .build())
          .build()
      ))
      .build();
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(SOME_CUSTOMER_ENTITY));
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
    when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(SOME_PRODUCT_ENTITY));
    Sell result = instance.convertToNewEntity(input);
    assertEquals(SOME_ADDRESS_ENTITY, result.getBillingAddress());
    verify(addressesRepositoryMock).findByFields(ANY, ANY, ANY, ANY, ANY, ANY);
    verifyNoInteractions(addressesConverterServiceMock);
  }

  @Test
  void converts_to_new_entity_with_new_billing_company() throws BadInputException {
    SellPojo input = SellPojo.builder()
      .date(SOME_INSTANT)
      .customer(SOME_CUSTOMER)
      .billingType(BILLING_TYPE_ENTERPRISE)
      .billingCompany(SOME_BILLING_COMPANY)
      .billingAddress(SOME_ADDRESS)
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(ProductPojo.builder()
            .barcode(ANY)
            .build())
          .build()
      ))
      .build();
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(SOME_CUSTOMER_ENTITY));
    when(billingCompaniesConverterServiceMock.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(SOME_BILLING_COMPANY_ENTITY);
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
    when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(SOME_PRODUCT_ENTITY));
    Sell result = instance.convertToNewEntity(input);
    assertEquals(SOME_BILLING_COMPANY_ENTITY, result.getBillingCompany());
    verify(billingCompaniesCrudServiceMock).getExisting(SOME_BILLING_COMPANY);
    verify(billingCompaniesConverterServiceMock).convertToNewEntity(SOME_BILLING_COMPANY);
  }

  @Test
  void converts_to_new_entity_with_existing_billing_company() throws BadInputException {
    SellPojo input = SellPojo.builder()
      .date(SOME_INSTANT)
      .customer(SOME_CUSTOMER)
      .billingType(BILLING_TYPE_ENTERPRISE)
      .billingCompany(SOME_BILLING_COMPANY)
      .billingAddress(SOME_ADDRESS)
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(ProductPojo.builder()
            .barcode(ANY)
            .build())
          .build()
      ))
      .build();
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(SOME_CUSTOMER_ENTITY));
    when(billingCompaniesCrudServiceMock.getExisting(any(BillingCompanyPojo.class))).thenReturn(Optional.of(SOME_BILLING_COMPANY_ENTITY));
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
    when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(SOME_PRODUCT_ENTITY));
    Sell result = instance.convertToNewEntity(input);
    assertEquals(SOME_BILLING_COMPANY_ENTITY, result.getBillingCompany());
    verify(billingCompaniesCrudServiceMock).getExisting(SOME_BILLING_COMPANY);
    verifyNoInteractions(billingCompaniesConverterServiceMock);
  }

  @Nested
  class NewEntityValidationFailureCases {
    private static final String NO_CUSTOMER_PROVIDED = "No customer provided";
    private static final String NO_BILLING_COMPANY_PROVIDED = "No billing company provided";

    @Test
    void no_customer_information() throws BadInputException {
      when(customersCrudServiceMock.getExisting(isNull())).thenThrow(new BadInputException(NO_CUSTOMER_PROVIDED));
      List.of(
        SellPojo.builder()
          .date(null)
          .build(),
        SellPojo.builder()
          .date(SOME_INSTANT)
          .build(),
        SellPojo.builder()
          .date(SOME_INSTANT)
          .customer(null)
          .build()
      ).forEach(input -> {
        BadInputException result = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(input));
        assertEquals(NO_CUSTOMER_PROVIDED, result.getMessage());
      });
      verifyNoInteractions(customersConverterServiceMock);
    }

    @Test
    void no_billing_information() {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(null)
        .billingAddress(null)
        .build();
      BadInputException result = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(input));
      assertEquals(UNEXISTING_BILLING_TYPE, result.getMessage());
      verify(billingTypesRepositoryMock).findByName(isNull());
    }

    @Test
    void invalid_billing_type() {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(NOT_ANY)
        .billingAddress(null)
        .build();
      when(billingTypesRepositoryMock.findByName(NOT_ANY)).thenReturn(Optional.empty());
      BadInputException result = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(input));
      assertEquals(UNEXISTING_BILLING_TYPE, result.getMessage());
      verify(billingTypesRepositoryMock).findByName(NOT_ANY);
    }

    @Test
    void billing_type_without_billing_address() {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(ANY)
        .billingAddress(null)
        .build();
      BillingType existingBillingType = BillingType.builder().build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingBillingType));
      assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(input));
      verify(billingTypesRepositoryMock).findByName(ANY);
    }

    @Test
    void billing_type_for_enterprise_without_billing_company() throws BadInputException {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(BILLING_TYPE_ENTERPRISE)
        .billingAddress(AddressPojo.builder()
          .firstLine(ANY)
          .secondLine(ANY)
          .city(ANY)
          .municipality(ANY)
          .postalCode(ANY)
          .notes(ANY)
          .build())
        .billingCompany(null)
        .build();
      BillingType existingBillingType = BillingType.builder().build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingBillingType));
      when(billingCompaniesCrudServiceMock.getExisting(isNull())).thenThrow(new BadInputException(NO_BILLING_COMPANY_PROVIDED));
      BadInputException result = assertThrows(BadInputException.class, () -> instance.convertToNewEntity(input));
      assertEquals(NO_BILLING_COMPANY_PROVIDED, result.getMessage());
    }

    @Test
    void no_details() {
      SellPojo inputWithoutDetails = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(ANY)
        .billingAddress(AddressPojo.builder()
          .firstLine(ANY)
          .secondLine(ANY)
          .city(ANY)
          .municipality(ANY)
          .postalCode(ANY)
          .notes(ANY)
          .build())
        .details(null)
        .build();
      BillingType existingBillingType = BillingType.builder().build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingBillingType));
      when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.empty());
      assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(inputWithoutDetails));
    }

    // TODO finish this test suite
    // @Test
    // void insufficient_or_invalid_details() {
    //   SellPojo inputWithZeroDetails = SellPojo.builder()
    //     .date(SOME_INSTANT)
    //     .customer(SOME_CUSTOMER)
    //     .billingType(ANY)
    //     .billingAddress(AddressPojo.builder()
    //       .firstLine(ANY)
    //       .secondLine(ANY)
    //       .city(ANY)
    //       .municipality(ANY)
    //       .postalCode(ANY)
    //       .notes(ANY)
    //       .build())
    //     .details(List.of())
    //     .build();
    //   when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.empty());
    //   assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(inputWithZeroDetails));
    // }

    // TODO add suites to validate
    //  - proper details
    //  - shipping information
    //  - billing to companies
    //  - total values calculation
  }
}
