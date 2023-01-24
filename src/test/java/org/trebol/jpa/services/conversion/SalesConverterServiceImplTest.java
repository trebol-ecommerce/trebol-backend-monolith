package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.*;
import org.trebol.pojo.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesConverterServiceImplTest {
  @InjectMocks SalesConverterServiceImpl sut;
  @Mock ICustomersConverterService customersConverter;
  @Mock ISalespeopleConverterService salespeopleConverter;
  @Mock IBillingCompaniesConverterService billingCompaniesConverter;
  @Mock IAddressesConverterService addressesConverterService;
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
