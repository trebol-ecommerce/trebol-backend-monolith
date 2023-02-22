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

package org.trebol.jpa.services.patch.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.BillingCompanyPojo;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.SellPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.*;
import org.trebol.common.services.RegexMatcherAdapterService;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.testing.SalesTestHelper;

import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesPatchServiceImplTest {
  @InjectMocks SalesPatchServiceImpl instance;
  @Mock SellStatusesRepository statusesRepositoryMock;
  @Mock BillingTypesRepository billingTypesRepositoryMock;
  @Mock PaymentTypesRepository paymentTypesRepositoryMock;
  @Mock BillingCompaniesRepository billingCompaniesRepositoryMock;
  @Mock ShippersRepository shippersRepositoryMock;
  @Mock AddressesRepository addressesRepositoryMock;
  @Mock BillingCompaniesConverterService billingCompaniesConverterMock;
  @Mock CustomersConverterService customersConverterMock;
  @Mock CustomersCrudService customersServiceMock;
  @Mock CustomersRepository customersRepositoryMock;
  @Mock AddressesConverterService addressesConverterServiceMock;
  @Mock Validator validatorMock;
  @Mock RegexMatcherAdapterService regexMatcherAdapterServiceMock;
  SalesTestHelper salesTestHelper = new SalesTestHelper();
  BillingType billingType;
  Sell existingSell;

  @BeforeEach
  public void beforeEach() {
    salesTestHelper.resetSales();
    existingSell = salesTestHelper.sellEntityAfterCreation();
    billingType = new BillingType(ID_1L, ANY);
  }

  @Test
  void accepts_empty_object() {
    SellPojo emptyObject = SellPojo.builder().build();
    assertDoesNotThrow(() -> instance.patchExistingEntity(emptyObject, existingSell));
  }

  @Test
  void only_accepts_sell_statuses_stored_in_persistence_layer() {
    SellPojo sellPojo = SellPojo.builder()
      .status(ANY)
      .build();
    when(statusesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Status '" + sellPojo.getStatus() + "' is not valid", result.getMessage());

    SellStatus sellStatus = new SellStatus(ID_1L, 1, ANY);
    when(statusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(sellStatus));
    assertDoesNotThrow(() -> instance.patchExistingEntity(sellPojo, existingSell));
  }

  @Test
  void only_accepts_payment_types_stored_in_persistence_layer() {
    SellPojo sellPojo = SellPojo.builder()
      .paymentType(ANY)
      .build();
    when(paymentTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Payment type '" + sellPojo.getPaymentType() + "' is not valid", result.getMessage());

    PaymentType paymentType = new PaymentType(ID_1L, ANY);
    when(paymentTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(paymentType));
    assertDoesNotThrow(() -> instance.patchExistingEntity(sellPojo, existingSell));
  }

  @Test
  void only_accepts_billing_types_stored_in_persistence_layer() {
    SellPojo sellPojo = SellPojo.builder()
      .billingType(ANY)
      .build();
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Billing type '" + sellPojo.getBillingType() + "' is not valid", result.getMessage());

    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(billingType));
    assertDoesNotThrow(() -> instance.patchExistingEntity(sellPojo, existingSell));
  }

  @Test
  void does_not_process_enterprise_invoices_without_company_data() {
    SellPojo sellPojo = SellPojo.builder()
      .billingType(BILLING_TYPE_ENTERPRISE)
      .build();
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(billingType));
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Billing company details are required to generate enterprise invoices", result.getMessage());
  }

  @Test
  void does_not_process_enterprise_invoices_with_empty_company_data() {
    SellPojo sellPojo = SellPojo.builder()
      .billingType(BILLING_TYPE_ENTERPRISE)
      .billingCompany(BillingCompanyPojo.builder().build())
      .build();
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(billingType));
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Billing company must have an id number", result.getMessage());
  }

  @Test
  void does_not_process_enterprise_invoices_with_invalid_company_data() {
    SellPojo sellPojo = SellPojo.builder()
      .billingType(BILLING_TYPE_ENTERPRISE)
      .billingCompany(BillingCompanyPojo.builder()
        .idNumber(ANY)
        .build())
      .build();
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(billingType));
    when(regexMatcherAdapterServiceMock.isAValidIdNumber(anyString())).thenReturn(false);
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Billing company must have a correct id number", result.getMessage());
    verify(regexMatcherAdapterServiceMock).isAValidIdNumber(sellPojo.getBillingCompany().getIdNumber());

    when(regexMatcherAdapterServiceMock.isAValidIdNumber(anyString())).thenReturn(true);
    assertDoesNotThrow(() -> instance.patchExistingEntity(sellPojo, existingSell));
  }

  @Test
  void only_accepts_valid_customer_data() throws BadInputException {
    SellPojo sellPojo = SellPojo.builder()
      .customer(CustomerPojo.builder()
        .person(PersonPojo.builder().build())
        .build())
      .build();
    BadInputException result = assertThrows(BadInputException.class, () -> instance.patchExistingEntity(sellPojo, existingSell));
    assertEquals("Customer must possess valid personal information", result.getMessage());

    sellPojo.setCustomer(CustomerPojo.builder()
      .person(PersonPojo.builder()
        .idNumber(ANY)
        .build())
      .build());
    Customer existingCustomer = Customer.builder()
      .person(Person.builder()
        .idNumber(ANY)
        .build())
      .build();
    when(customersServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(existingCustomer));
    Sell succesfulResult = instance.patchExistingEntity(sellPojo, existingSell);
    assertEquals(sellPojo.getCustomer().getPerson().getIdNumber(), succesfulResult.getCustomer().getPerson().getIdNumber());
  }
}
