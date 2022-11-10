package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.config.ValidationProperties;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class SalesConverterJpaServiceImplTest {
    
    @InjectMocks SalesConverterJpaServiceImpl sut;
    @Mock ITwoWayConverterJpaService<CustomerPojo, Customer> customersConverter;
    @Mock ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverter;
    @Mock ConversionService conversion;
    @Mock ValidationProperties validationProperties;

    private SellPojo sellPojo;
    private Sell sell;

    @BeforeEach
    public void beforeEach() {
        when(validationProperties.getIdNumberRegexp()).thenReturn(ANY);
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
}
