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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
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
  void converts_to_new_entity_with_existing_data() throws BadInputException {
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
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
    when(billingCompaniesCrudServiceMock.getExisting(any(BillingCompanyPojo.class))).thenReturn(Optional.of(SOME_BILLING_COMPANY_ENTITY));
    when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(SOME_PRODUCT_ENTITY));
    Sell result = instance.convertToNewEntity(input);
    assertEquals(SOME_CUSTOMER_ENTITY, result.getCustomer());
    verify(customersCrudServiceMock).getExisting(SOME_CUSTOMER);
    assertEquals(SOME_BILLING_TYPE_ENTITY, result.getBillingType());
    verify(billingTypesRepositoryMock).findByName(BILLING_TYPE_ENTERPRISE);
    assertEquals(SOME_BILLING_COMPANY_ENTITY, result.getBillingCompany());
    verify(billingCompaniesCrudServiceMock).getExisting(SOME_BILLING_COMPANY);
    assertEquals(SOME_ADDRESS_ENTITY, result.getBillingAddress());
    verify(addressesRepositoryMock, times(1)).findByFields(ANY, ANY, ANY, ANY, ANY, ANY);
    assertFalse(result.getDetails().isEmpty());
    assertEquals(1, result.getDetails().size());
    assertEquals(SOME_PRODUCT_ENTITY, result.getDetails().iterator().next().getProduct());
    verify(productsRepositoryMock, times(1)).findByBarcode(ANY);
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
    verify(addressesRepositoryMock).findByFields(ANY, ANY, ANY, ANY, ANY, ANY); // not mocked, is Optional.empty()
    verify(addressesConverterServiceMock).convertToNewEntity(input.getBillingAddress());
    assertEquals(SOME_ADDRESS_ENTITY, result.getBillingAddress());
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
    verify(billingCompaniesCrudServiceMock).getExisting(SOME_BILLING_COMPANY); // not mocked, is Optional.empty()
    verify(billingCompaniesConverterServiceMock).convertToNewEntity(SOME_BILLING_COMPANY);
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
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
      assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(input));
      verify(billingTypesRepositoryMock).findByName(ANY);
    }

    @Test
    void billing_type_for_enterprise_without_billing_company() throws BadInputException {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(BILLING_TYPE_ENTERPRISE)
        .billingAddress(SOME_ADDRESS)
        .billingCompany(null)
        .build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
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
        .billingAddress(SOME_ADDRESS)
        .details(null)
        .build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
      when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.empty());
      assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(inputWithoutDetails));
      verifyNoInteractions(productsRepositoryMock);
    }

    @Test
    void invalid_details() throws BadInputException {
      SellPojo.SellPojoBuilder inputBuilder = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(ANY)
        .billingAddress(SOME_ADDRESS);
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
      when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));

      // products cannot be null
      List.of(
        List.of(
          SellDetailPojo.builder().build()
        ),
        List.of(
          SellDetailPojo.builder()
            .product(null)
            .build()
        )
      ).forEach(detailsList -> {
        inputBuilder.details(detailsList);
        assertThrows(NullPointerException.class, () -> instance.convertToNewEntity(inputBuilder.build()));
      });

      // nor can product barcodes be absent, null nor empty
      List.of(
        List.of(
          SellDetailPojo.builder()
            .product(ProductPojo.builder().build())
            .build()
        ),
        List.of(
          SellDetailPojo.builder()
            .product(ProductPojo.builder()
              .barcode(null)
              .build())
            .build()
        ),
        List.of(
          SellDetailPojo.builder()
            .product(ProductPojo.builder()
              .barcode("")
              .build())
            .build()
        )
      ).forEach(detailsList -> {
        inputBuilder.details(detailsList);
        RuntimeException result = assertThrows(RuntimeException.class, () -> instance.convertToNewEntity(inputBuilder.build()));
        assertEquals("Product barcode must be valid", result.getMessage());
      });
      verifyNoInteractions(productsRepositoryMock);
    }

    @Test
    void invalid_products() throws BadInputException {
      SellPojo input = SellPojo.builder()
        .date(SOME_INSTANT)
        .customer(SOME_CUSTOMER)
        .billingType(ANY)
        .billingAddress(SOME_ADDRESS)
        .details(List.of(
          SellDetailPojo.builder()
            .product(ProductPojo.builder()
              .barcode(ANY)
              .build())
            .build()
        ))
        .build();
      when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(SOME_BILLING_TYPE_ENTITY));
      when(addressesRepositoryMock.findByFields(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(Optional.of(SOME_ADDRESS_ENTITY));
      when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.empty());
      RuntimeException result = assertThrows(RuntimeException.class, () -> instance.convertToNewEntity(input));
      BadInputException originalResult = (BadInputException) result.getCause();
      assertEquals("Unexisting product in sell details", originalResult.getMessage());
      verify(productsRepositoryMock).findByBarcode(ANY);
    }

    // TODO add suites to validate
    //  - shipping information
    //  - total values calculation
  }
}
