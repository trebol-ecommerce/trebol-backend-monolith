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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.*;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.services.conversion.*;
import org.trebol.testing.SalesTestHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesConverterServiceImplTest {
  @InjectMocks SalesConverterServiceImpl instance;
  @Mock CustomersConverterService customersConverterMock;
  @Mock SalespeopleConverterService salespeopleConverterMock;
  @Mock BillingCompaniesConverterService billingCompaniesConverterMock;
  @Mock AddressesConverterService addressesConverterServiceMock;
  @Mock ShippersConverterService shippersConverterServiceMock;
  final SalesTestHelper salesTestHelper = new SalesTestHelper();

  @BeforeEach
  void beforeEach() {
    salesTestHelper.resetSales();
  }

  @Test
  void converts_to_pojo_with_some_data() {
    Sell sell = salesTestHelper.sellEntityBeforeCreation();
    CustomerPojo expectedCustomerPojo = CustomerPojo.builder().build();
    AddressPojo expectedAddressPojo = AddressPojo.builder().build();
    when(customersConverterMock.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());
    when(addressesConverterServiceMock.convertToPojo(any(Address.class))).thenReturn(expectedAddressPojo);
    SellPojo result = instance.convertToPojo(sell);
    assertNotNull(result);
    assertEquals(sell.getDate(), result.getDate());
    assertEquals(expectedCustomerPojo, result.getCustomer());
    assertEquals(sell.getStatus().getName(), result.getStatus());
    assertEquals(sell.getPaymentType().getName(), result.getPaymentType());
    assertEquals(sell.getBillingType().getName(), result.getBillingType());
    assertEquals(expectedAddressPojo, result.getBillingAddress());
    assertNull(result.getBillingCompany());
    assertNull(result.getSalesperson());
    assertNull(result.getShippingAddress());
    assertNull(result.getShipper());
    verify(customersConverterMock).convertToPojo(sell.getCustomer());
    verify(addressesConverterServiceMock).convertToPojo(sell.getBillingAddress());
  }

  @Test
  void converts_to_pojo_with_all_data() {
    Sell sell = salesTestHelper.sellEntityAfterCreation();
    sell.setShipper(Shipper.builder().build());
    sell.setShippingAddress(Address.builder().build());
    sell.setSalesperson(Salesperson.builder().build());
    AddressPojo expectedAddress = AddressPojo.builder().build();
    ShipperPojo expectedShipperPojo = ShipperPojo.builder().build();
    SalespersonPojo expectedSalespersonPojo = SalespersonPojo.builder().build();
    when(customersConverterMock.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());
    when(addressesConverterServiceMock.convertToPojo(any(Address.class))).thenReturn(expectedAddress);
    when(shippersConverterServiceMock.convertToPojo(any(Shipper.class))).thenReturn(expectedShipperPojo);
    when(salespeopleConverterMock.convertToPojo(any(Salesperson.class))).thenReturn(expectedSalespersonPojo);
    SellPojo result = instance.convertToPojo(sell);
    assertEquals(expectedAddress, result.getShippingAddress());
    assertEquals(expectedShipperPojo, result.getShipper());
    assertEquals(expectedSalespersonPojo, result.getSalesperson());
    verify(addressesConverterServiceMock, times(2)).convertToPojo(any(Address.class));
    verify(shippersConverterServiceMock).convertToPojo(sell.getShipper());
    verify(salespeopleConverterMock).convertToPojo(sell.getSalesperson());
  }

  @Test
  void only_converts_billing_company_data_if_billing_type_is_correct() {
    Sell sell = salesTestHelper.sellEntityBeforeCreation();
    sell.setBillingCompany(BillingCompany.builder()
      .id(ID_1L)
      .idNumber(ANY)
      .name(ANY)
      .build());
    when(customersConverterMock.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());
    when(addressesConverterServiceMock.convertToPojo(any(Address.class))).thenReturn(AddressPojo.builder().build());
    SellPojo result = instance.convertToPojo(sell);
    assertNull(result.getBillingCompany());
    verifyNoInteractions(billingCompaniesConverterMock);

    sell.setBillingType(BillingType.builder()
      .id(2L)
      .name(BILLING_TYPE_ENTERPRISE)
      .build());
    BillingCompanyPojo expectedBillingCompanyPojo = BillingCompanyPojo.builder().build();
    when(billingCompaniesConverterMock.convertToPojo(any(BillingCompany.class))).thenReturn(expectedBillingCompanyPojo);
    result = instance.convertToPojo(sell);
    assertNotNull(result.getBillingCompany());
    assertEquals(expectedBillingCompanyPojo, result.getBillingCompany());
    verify(billingCompaniesConverterMock).convertToPojo(sell.getBillingCompany());
  }
}
