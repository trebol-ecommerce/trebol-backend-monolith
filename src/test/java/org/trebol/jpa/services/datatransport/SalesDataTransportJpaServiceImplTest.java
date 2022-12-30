package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.config.ValidationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.*;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IBillingCompaniesConverterJpaService;
import org.trebol.jpa.services.conversion.ICustomersConverterJpaService;
import org.trebol.pojo.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesDataTransportJpaServiceImplTest {
    @InjectMocks SalesDataTransportJpaServiceImpl sut;
    @Mock ISellStatusesJpaRepository statusesRepository;
    @Mock IBillingTypesJpaRepository billingTypesRepository;
    @Mock IPaymentTypesJpaRepository paymentTypesRepository;
    @Mock IBillingCompaniesJpaRepository billingCompaniesRepository;
    @Mock IShippersJpaRepository shippersRepository;
    @Mock IAddressesJpaRepository addressesRepository;
    @Mock IBillingCompaniesConverterJpaService billingCompaniesConverter;
    @Mock ICustomersConverterJpaService customersConverter;
    @Mock GenericCrudJpaService<CustomerPojo, Customer> customersService;
    @Mock ICustomersJpaRepository customersRepository;
    @Mock ConversionService conversion;
    @Mock Validator validator;
    @Mock ValidationProperties validationProperties;
    SellPojo sellPojo;
    Sell sell;

    @BeforeEach
    public void beforeEach() {
      when(validationProperties.getIdNumberRegexp()).thenReturn(".");
      sut = new SalesDataTransportJpaServiceImpl(
        conversion,
        statusesRepository,
        billingTypesRepository,
        billingCompaniesRepository,
        paymentTypesRepository,
        addressesRepository,
        shippersRepository,
        billingCompaniesConverter,
        customersConverter,
        customersService,
        customersRepository,
        validator,
        validationProperties
      );
        sellPojo = SellPojo.builder().build();
        sell = new Sell();
    }

    @Test
    void sanity_check() {
      assertNotNull(sut);
    }

    @Test
    void testConvertToNewEntityApplyStatusBadInputException() {
        sellPojo.setStatus(ANY);
        sellPojo.setDate(Instant.now());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.empty());

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Status 'ANY' is not valid", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyPaymentTypeBadInputException() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("An accepted payment type is required", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyPaymentTypeBadInputException2() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.empty());

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Payment type 'ANY' is not valid", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyBadInputException() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Billing type 'Bill' is not valid", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyBadInputException() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Billing company details are required to generate enterprise invoices", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyBadInputException2() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Billing company must have an id number", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyfetchOrConvertBillingCompanyBadInputException() {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));

        assertEquals("Billing company must have a correct id number", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyfetchOrConvertBillingCompanyBadInputException2() {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.of(billingCompany));


        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
        assertEquals("Customer must possess valid personal information", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyCustomerConvertDetailsBadInputException() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
        sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build());
        sellPojo.setDetails(List.of(SellDetailPojo.builder()
                                      .product(ProductPojo.builder().build())
                                      .build()));

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());
        when(billingCompaniesConverter.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(billingCompany);
        when(billingCompaniesRepository.saveAndFlush(any(BillingCompany.class))).thenReturn(billingCompany);
        final Person person = new Person();
        person.setId(ID_1L);
        person.setLastName(ANY);
        final Customer customer = new Customer(person);
        when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(customer));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
        assertEquals("Product barcode must be valid", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetailsBadInputException2() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
        sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build());
        sellPojo.setDetails(List.of(SellDetailPojo.builder()
                                      .product(ProductPojo.builder().barcode(ANY).build())
                                      .build()));

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());
        when(billingCompaniesConverter.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(billingCompany);
        when(billingCompaniesRepository.saveAndFlush(any(BillingCompany.class))).thenReturn(billingCompany);
        final Person person = new Person();
        person.setId(ID_1L);
        person.setLastName(ANY);
        final Customer customer = new Customer(person);
        when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(customer));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.applyChangesToExistingEntity(sellPojo, sell));
        assertEquals("Unexisting product in sell details", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetails() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
        sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build());
        sellPojo.setDetails(List.of(SellDetailPojo.builder()
                                      .product(ProductPojo.builder().barcode(ANY).build())
                                      .units(1)
                                      .build()));

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());
        when(billingCompaniesConverter.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(billingCompany);
        when(billingCompaniesRepository.saveAndFlush(any(BillingCompany.class))).thenReturn(billingCompany);
        final Person person = new Person();
        person.setId(ID_1L);
        person.setLastName(ANY);
        final Customer customer = new Customer(person);
        when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(customer));

        Sell actual = sut.applyChangesToExistingEntity(sellPojo, sell);

        assertNotNull(actual.getDetails());
    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetails2() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
        sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build());
        sellPojo.setDetails(List.of(SellDetailPojo.builder()
                                      .product(ProductPojo.builder().barcode(ANY).build())
                                      .units(1)
                                      .build()));

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());
        when(billingCompaniesConverter.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(billingCompany);
        when(billingCompaniesRepository.saveAndFlush(any(BillingCompany.class))).thenReturn(billingCompany);
        final Person person = new Person();
        person.setId(ID_1L);
        person.setLastName(ANY);
        final Customer customer = new Customer(person);
        when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.empty());
        when(customersConverter.convertToNewEntity(any(CustomerPojo.class))).thenReturn(customer);
        when(customersRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        Sell actual = sut.applyChangesToExistingEntity(sellPojo, sell);

        assertNotNull(actual.getDetails());
    }


    @Test
    void testConvertToNewEntityApplyBillingAddress() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());
        sellPojo.setCustomer(CustomerPojo.builder().person(PersonPojo.builder().idNumber(ANY).build()).build()); // TODO refactor this inline CustomerPojo, there's 6 of these
        sellPojo.setDetails(List.of(SellDetailPojo.builder()
                                      .product(ProductPojo.builder().barcode(ANY).build())
                                      .units(1)
                                      .build()));
        sellPojo.setBillingAddress(AddressPojo.builder().build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));
        final BillingCompany billingCompany = new BillingCompany(ID_1L, ANY, ANY);
        when(billingCompaniesRepository.findByIdNumber(anyString())).thenReturn(Optional.empty());
        when(billingCompaniesConverter.convertToNewEntity(any(BillingCompanyPojo.class))).thenReturn(billingCompany);
        when(billingCompaniesRepository.saveAndFlush(any(BillingCompany.class))).thenReturn(billingCompany);
        final Person person = new Person();
        person.setId(ID_1L);
        person.setLastName(ANY);
        final Customer customer = new Customer(person);
        when(customersService.getExisting(any(CustomerPojo.class))).thenReturn(Optional.of(customer));
        Set<ConstraintViolation<AddressPojo>> violations = new HashSet<>();

//        Set<ConstraintViolation<AddressPojo>> validations = Mockito.mock(Set<ConstraintViolation.class>);
        when(validator.validate(any(AddressPojo.class))).thenReturn(violations);

        Sell actual = sut.applyChangesToExistingEntity(sellPojo, sell);

        assertEquals(person.getId(), actual.getCustomer().getPerson().getId());
    }
}
