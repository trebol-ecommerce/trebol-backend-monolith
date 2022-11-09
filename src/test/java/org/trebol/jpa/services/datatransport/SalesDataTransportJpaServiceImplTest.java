package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.config.ValidationProperties;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.*;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingCompanyPojo;
import org.trebol.pojo.CustomerPojo;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesDataTransportJpaServiceImplTest {
    
    SalesDataTransportJpaServiceImpl sut;

    @Mock ISellStatusesJpaRepository statusesRepository;
    @Mock IBillingTypesJpaRepository billingTypesRepository;
    @Mock IPaymentTypesJpaRepository paymentTypesRepository;
    @Mock IBillingCompaniesJpaRepository billingCompaniesRepository;
    @Mock IShippersJpaRepository shippersRepository;
    @Mock IAddressesJpaRepository addressesRepository;
    @Mock ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverter;
    @Mock ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter;
    @Mock GenericCrudJpaService<CustomerPojo, Customer> customersService;
    @Mock ICustomersJpaRepository customersRepository;
    @Mock IProductsJpaRepository productsRepository;
    @Mock ConversionService conversion;
    @Mock Validator validator;
    @Mock ValidationProperties validationProperties;

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
        productsRepository,
        validator,
        validationProperties
      );
    }

    @Test
    void sanity_check() {
      assertNotNull(sut);
    }
}
