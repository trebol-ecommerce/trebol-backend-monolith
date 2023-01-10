package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.pojo.BillingCompanyPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingCompaniesConverterJpaServiceImplTest {
    @InjectMocks BillingCompaniesConverterJpaServiceImpl sut;
    BillingCompany billingCompany;
    BillingCompanyPojo billingCompanyPojo;

    @BeforeEach
    void beforeEach() {
        billingCompany = new BillingCompany();
        billingCompany.setName(ANY);
        billingCompany.setId(1L);
        billingCompany.setIdNumber(ANY);

        billingCompanyPojo = BillingCompanyPojo.builder()
          .idNumber(ANY)
          .name(ANY)
          .build();
    }

    @AfterEach
    void afterEach() {
        billingCompany = null;
        billingCompanyPojo = null;
    }
    @Test
    void testConvertToPojo() {
        BillingCompanyPojo actual = sut.convertToPojo(billingCompany);
        assertEquals(billingCompany.getIdNumber(), actual.getIdNumber());
        assertEquals(billingCompany.getName(), actual.getName());
    }

    @Test
    void testConvertToNewEntity() {
        BillingCompany actual = sut.convertToNewEntity(billingCompanyPojo);
        assertEquals(billingCompanyPojo.getIdNumber(), actual.getIdNumber());
        assertEquals(billingCompanyPojo.getName(), actual.getName());
    }
}
