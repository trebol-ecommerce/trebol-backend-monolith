package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.pojo.BillingCompanyPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingCompaniesDataTransportJpaServiceImplTest {
    @InjectMocks BillingCompaniesDataTransportJpaServiceImpl sut;
    private BillingCompany billingCompany;
    private BillingCompanyPojo billingCompanyPojo;

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
    void testApplyChangesToExistingEntity() throws BadInputException {
        billingCompanyPojo.setName("PIOLO");
        BillingCompany actual = sut.applyChangesToExistingEntity(billingCompanyPojo, billingCompany);
        assertEquals(1L, actual.getId());
    }
}
