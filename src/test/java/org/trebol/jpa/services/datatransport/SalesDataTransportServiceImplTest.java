package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.*;
import org.trebol.jpa.services.conversion.AddressesConverterService;
import org.trebol.jpa.services.conversion.BillingCompaniesConverterService;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.jpa.services.helpers.RegexMatcherAdapterService;
import org.trebol.pojo.BillingCompanyPojo;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SellPojo;

import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.config.Constants.BILLING_TYPE_ENTERPRISE;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesDataTransportServiceImplTest {
  @InjectMocks SalesDataTransportServiceImpl sut;
  @Mock SellStatusesJpaRepository statusesRepository;
  @Mock BillingTypesJpaRepository billingTypesRepository;
  @Mock PaymentTypesJpaRepository paymentTypesRepository;
  @Mock BillingCompaniesJpaRepository billingCompaniesRepository;
  @Mock ShippersJpaRepository shippersRepository;
  @Mock AddressesJpaRepository addressesRepository;
  @Mock BillingCompaniesConverterService billingCompaniesConverter;
  @Mock CustomersConverterService customersConverter;
  @Mock CustomersCrudService customersService;
  @Mock CustomersJpaRepository customersRepository;
  @Mock AddressesConverterService addressesConverterService;
  @Mock Validator validator;
  @Mock RegexMatcherAdapterService regexMatcherAdapterService;
  SellPojo sellPojo;
  Sell sell;
  SellStatus sellStatus;
  PaymentType paymentType;
  BillingType billingType;
  BillingCompany billingCompany;

  @BeforeEach
  public void beforeEach() {
    sellPojo = SellPojo.builder().build();
    sell = new Sell();
    sellStatus = new SellStatus(ID_1L, 1, ANY);
    paymentType = new PaymentType(ID_1L, ANY);
    billingType = new BillingType(ID_1L, ANY);
    billingCompany = new BillingCompany(ID_1L, ANY, ANY);
  }

  @Test
  void accepts_empty_object() {
    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }

  @Test
  void only_accepts_sell_statuses_stored_in_persistence_layer() {
    sellPojo.setStatus("ANY");
    when(statusesRepository.findByName(anyString())).thenReturn(Optional.empty());

    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Status 'ANY' is not valid", badInputException.getMessage());

    when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(sellStatus));

    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }


  @Test
  void only_accepts_payment_types_stored_in_persistence_layer() {
    sellPojo.setPaymentType("ANY");
    when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.empty());

    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Payment type 'ANY' is not valid", badInputException.getMessage());

    when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(paymentType));

    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }


  @Test
  void only_accepts_billing_types_stored_in_persistence_layer() {
    sellPojo.setBillingType("ANY");
    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.empty());

    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Billing type 'ANY' is not valid", badInputException.getMessage());

    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(billingType));

    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }

  @Test
  void does_not_process_enterprise_invoices_without_company_data() {
    sellPojo.setBillingType(BILLING_TYPE_ENTERPRISE);
    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(billingType));

    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Billing company details are required to generate enterprise invoices", badInputException.getMessage());
  }

  @Test
  void does_not_process_enterprise_invoices_with_empty_company_data() {
    sellPojo.setBillingType(BILLING_TYPE_ENTERPRISE);
    sellPojo.setBillingCompany(BillingCompanyPojo.builder().build());
    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(billingType));

    BadInputException badInputException1 = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Billing company must have an id number", badInputException1.getMessage());
  }

  @Test
  void does_not_process_enterprise_invoices_with_invalid_company_data() {
    sellPojo.setBillingType(BILLING_TYPE_ENTERPRISE);
    sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(billingType));
    when(regexMatcherAdapterService.isAValidIdNumber(anyString())).thenReturn(false);

    BadInputException badInputException2 = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Billing company must have a correct id number", badInputException2.getMessage());
  }

  @Test
  void processes_enterprise_invoices_with_valid_company_data() {
    sellPojo.setBillingType(BILLING_TYPE_ENTERPRISE);
    sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
    when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(billingType));
    when(regexMatcherAdapterService.isAValidIdNumber(anyString())).thenReturn(true);

    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }

  @Test
  void only_accepts_valid_customer_data() throws BadInputException {
    sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().build()).build());
    BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
    assertEquals("Customer must possess valid personal information", badInputException.getMessage());

    sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build());
    Customer customer = new Customer(new Person());
    when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(customer));

    assertDoesNotThrow(() -> sut.applyChangesToExistingEntity(sellPojo, sell));
  }
}
