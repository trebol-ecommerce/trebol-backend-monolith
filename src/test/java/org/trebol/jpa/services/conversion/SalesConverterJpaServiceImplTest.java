package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.config.ValidationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Address;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.PaymentType;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.IAddressesJpaRepository;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.repositories.IPaymentTypesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesConverterJpaServiceImplTest {
    
    private SalesConverterJpaServiceImpl sut;


    @Mock
    private  ISellStatusesJpaRepository statusesRepository;
    @Mock
    private  IBillingTypesJpaRepository billingTypesRepository;
    @Mock
    private  IPaymentTypesJpaRepository paymentTypesRepository;
    @Mock
    private  IBillingCompaniesJpaRepository billingCompaniesRepository;
    @Mock
    private  IShippersJpaRepository shippersRepository;
    @Mock
    private  IAddressesJpaRepository addressesRepository;
    @Mock
    private  ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverter;
    @Mock
    private  ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter;
    @Mock
    private  GenericCrudJpaService<CustomerPojo, Customer> customersService;
    @Mock
    private  ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter;
    @Mock
    private  ICustomersJpaRepository customersRepository;
    @Mock
    private  IProductsJpaRepository productsRepository;
    @Mock
    private  ConversionService conversion;
    @Mock
    private  Validator validator;
    @Mock
    private  Pattern companyIdNumberPattern;

    @Mock
    ValidationProperties validationProperties;

    private SellPojo sellPojo;
    private Sell sell;

    @BeforeEach
    public void beforeEach() {
        when(validationProperties.getIdNumberRegexp()).thenReturn(ANY);
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                 billingTypesRepository,
                 billingCompaniesRepository,
                 paymentTypesRepository,
                 addressesRepository,
                 shippersRepository,
                 billingCompaniesConverter,
                 customersConverter,
                 customersService,
                  salespeopleConverter,
                 customersRepository,
                 productsRepository,
                 validator,
                 validationProperties);
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
        sell.setBillingType(new BillingType(ID_1L, "Enterprise Invoice"));
        sell.setBillingCompany(new BillingCompany(ID_1L, ANY, ANY));
        sell.setBillingAddress(new Address());
        sell.setShippingAddress(new Address());
        sell.setCustomer(new Customer(ANY));
        sell.setSalesperson(new Salesperson(ANY));
        when(conversion.convert(any(Sell. class), eq(SellPojo.class))).thenReturn(sellPojo);
        when(conversion.convert(any(BillingCompany.class), eq(BillingCompanyPojo.class))).thenReturn(BillingCompanyPojo.builder().build());
        when(conversion.convert(any(Address.class), eq(AddressPojo.class))).thenReturn(AddressPojo.builder().build());

        when(customersConverter.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());
        when(salespeopleConverter.convertToPojo(any(Salesperson.class))).thenReturn(SalespersonPojo.builder().build());

        SellPojo actual = sut.convertToPojo(sell);

        assertEquals(ANY, actual.getStatus());


        verify(conversion, times(2)).convert(any(Address.class), eq(AddressPojo.class));
        verify(customersConverter, times(1)).convertToPojo(any(Customer.class));
        verify(salespeopleConverter, times(1)).convertToPojo(any(Salesperson.class));
    }

    @Test
    void testConvertToPojoNullProperties() {
        sell.setStatus(new SellStatus(ID_1L, 1, ANY));
        sell.setPaymentType(new PaymentType(ID_1L, ANY));
        sell.setBillingType(new BillingType(ID_1L, "Enterprise Invoice"));
        sell.setCustomer(new Customer(ANY));
        when(conversion.convert(any(Sell. class), eq(SellPojo.class))).thenReturn(sellPojo);
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
        when(conversion.convert(any(Sell. class), eq(SellPojo.class))).thenReturn(sellPojo);
        when(customersConverter.convertToPojo(any(Customer.class))).thenReturn(CustomerPojo.builder().build());

        SellPojo actual = sut.convertToPojo(sell);

        assertEquals(ANY, actual.getStatus());
        verify(customersConverter, times(1)).convertToPojo(any(Customer.class));
    }

    @Test
    void testConvertToNewEntityApplyStatusBadInputException() throws BadInputException {
        sellPojo.setStatus(ANY);
        sellPojo.setDate(Instant.now());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.empty());

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Status 'ANY' is not valid", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyPaymentTypeBadInputException() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("An accepted payment type is required", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyPaymentTypeBadInputException2() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.empty());

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Payment type 'ANY' is not valid", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyBadInputException() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Billing type 'Bill' is not valid", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyBadInputException() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Billing company details are required to generate enterprise invoices", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyBadInputException2() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Billing company must have an id number", badInputException.getMessage());
    }


    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyfetchOrConvertBillingCompanyBadInputException() throws BadInputException {
        sellPojo.setStatus(null);
        sellPojo.setDate(Instant.now());
        sellPojo.setPaymentType(ANY);
        sellPojo.setBillingType("Enterprise Invoice");
        sellPojo.setBillingCompany(BillingCompanyPojo.builder().idNumber(ANY).build());

        when(statusesRepository.findByName(anyString())).thenReturn(Optional.of(new SellStatus(ID_1L, 1, ANY)));
        when(paymentTypesRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentType(ID_1L, ANY)));
        when(billingTypesRepository.findByName(anyString())).thenReturn(Optional.of(new BillingType(ID_1L, ANY)));

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));

        assertEquals("Billing company must have a correct id number", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyBillingTypeAndCompanyEnterpriseSourceBillingCompanyfetchOrConvertBillingCompanyBadInputException2() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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


        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));
        assertEquals("Customer must posess valid personal information", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntityApplyCustomerConvertDetailsBadInputException() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));
        assertEquals("Product barcode must be valid", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetailsBadInputException2() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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
        when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.empty());

        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(sellPojo));
        assertEquals("Unexisting product in sell details", badInputException.getMessage());

    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetails() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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
        final Product product = new Product(ANY);
        product.setPrice(1);
        when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.of(product));

        Sell actual = sut.convertToNewEntity(sellPojo);

        assertNotNull(actual.getDetails());
    }


    @Test
    void testConvertToNewEntityApplyCustomerConvertDetails2() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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
        final Product product = new Product(ANY);
        product.setPrice(1);
        when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.of(product));

        Sell actual = sut.convertToNewEntity(sellPojo);

        assertNotNull(actual.getDetails());
    }


    @Test
    void testConvertToNewEntityApplyBillingAddress() throws BadInputException {

        when(validationProperties.getIdNumberRegexp()).thenReturn("");
        sut = new SalesConverterJpaServiceImpl( conversion,
                statusesRepository,
                billingTypesRepository,
                billingCompaniesRepository,
                paymentTypesRepository,
                addressesRepository,
                shippersRepository,
                billingCompaniesConverter,
                customersConverter,
                customersService,
                salespeopleConverter,
                customersRepository,
                productsRepository,
                validator,
                validationProperties);

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
        final Product product = new Product(ANY);
        product.setPrice(1);
        when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.of(product));
        Set<ConstraintViolation<AddressPojo>> violations = new HashSet<>();

//        Set<ConstraintViolation<AddressPojo>> validations = Mockito.mock(Set<ConstraintViolation.class>);
        when(validator.validate(any(AddressPojo.class))).thenReturn(violations);

        Sell actual = sut.convertToNewEntity(sellPojo);

        assertEquals(person.getId(), actual.getCustomer().getPerson().getId());
    }
}
