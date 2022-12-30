package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.pojo.BillingTypePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingTypesDataTransportJpaServiceImplTest {
    @InjectMocks BillingTypesDataTransportJpaServiceImpl sut;

    private BillingType billingType;
    private BillingTypePojo billingTypePojo;

    @BeforeEach
    void beforeEach() {
        billingType = new BillingType();
        billingType.setName(ANY);
        billingType.setId(1L);
        billingType.setName(ANY);

        billingTypePojo = BillingTypePojo.builder()
          .name(ANY)
          .build();
    }

    @AfterEach
    void afterEach() {
        billingType = null;
        billingTypePojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        billingTypePojo.setName("PIOLO");
        BillingType actual = sut.applyChangesToExistingEntity(billingTypePojo, billingType);
        assertEquals(1L, actual.getId());
    }
}
