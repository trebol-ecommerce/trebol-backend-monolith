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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.*;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.conversion.SalespeopleConverterService;
import org.trebol.jpa.services.conversion.impl.SalesConverterServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesConverterServiceImplTest {
  @InjectMocks SalesConverterServiceImpl sut;
  @Mock CustomersConverterService customersConverter;
  @Mock SalespeopleConverterService salespeopleConverter;
  @Mock BillingCompaniesConverterService billingCompaniesConverter;
  @Mock AddressesConverterService addressesConverterService;
  SellPojo sellPojo;
  Sell sell;

  @BeforeEach
  public void beforeEach() {
    sellPojo = SellPojo.builder().build();
    sell = new Sell();
  }

  @AfterEach
  public void setUpAfter() {

    sut = null;
    sellPojo = null;
    sell = null;
  }

  @Test
  void testConvertToPojo() {
    sell.setStatus(new SellStatus(ID_1L, 1, ANY));
    sell.setPaymentType(new PaymentType(ID_1L, ANY));
    sell.setBillingType(new BillingType(ID_1L, BILLING_TYPE_ENTERPRISE));
    sell.setBillingCompany(new BillingCompany(ID_1L, ANY, ANY));
    sell.setBillingAddress(new Address());
    sell.setShippingAddress(new Address());
    sell.setCustomer(new Customer(ANY));
    sell.setSalesperson(new Salesperson(ANY));
    when(billingCompaniesConverter.convertToPojo(any(BillingCompany.class))).thenReturn(BillingCompanyPojo.builder().build());
    when(addressesConverterService.convertToPojo(any(Address.class))).thenReturn(AddressPojo.builder().build());
    when(customersConverter.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());
    when(salespeopleConverter.convertToPojo(any(Salesperson.class))).thenReturn(SalespersonPojo.builder().build());

    SellPojo actual = sut.convertToPojo(sell);

    assertEquals(ANY, actual.getStatus());
    verify(addressesConverterService, times(2)).convertToPojo(any(Address.class));
    verify(customersConverter, times(1)).convertToPojo(any(Customer.class));
    verify(salespeopleConverter, times(1)).convertToPojo(any(Salesperson.class));
  }

  @Test
  void testConvertToPojoNullProperties() {
    sell.setStatus(new SellStatus(ID_1L, 1, ANY));
    sell.setPaymentType(new PaymentType(ID_1L, ANY));
    sell.setBillingType(new BillingType(ID_1L, BILLING_TYPE_ENTERPRISE));
    sell.setCustomer(new Customer(ANY));
    when(customersConverter.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());

    SellPojo actual = sut.convertToPojo(sell);

    assertEquals(ANY, actual.getStatus());
    verify(customersConverter, times(1)).convertToPojo(any(Customer.class));
  }

  @Test
  void testConvertToPojoNullPropertiesNotEnterpriceInvoice() {
    sell.setStatus(new SellStatus(ID_1L, 1, ANY));
    sell.setPaymentType(new PaymentType(ID_1L, ANY));
    sell.setBillingType(new BillingType(ID_1L, "Enterprise Invoicesss"));
    sell.setCustomer(new Customer(ANY));
    when(customersConverter.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());

    SellPojo actual = sut.convertToPojo(sell);

    assertEquals(ANY, actual.getStatus());
    verify(customersConverter, times(1)).convertToPojo(any(Customer.class));
  }
}
