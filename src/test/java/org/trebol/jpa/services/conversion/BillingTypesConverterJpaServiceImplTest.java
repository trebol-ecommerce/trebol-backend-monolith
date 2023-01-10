package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.BillingType;
import org.trebol.pojo.BillingTypePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingTypesConverterJpaServiceImplTest {
    @InjectMocks BillingTypesConverterJpaServiceImpl sut;
    BillingType billingType;
    BillingTypePojo billingTypePojo;

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
    void testConvertToPojo() {
        BillingTypePojo actual = sut.convertToPojo(billingType);
        assertEquals(billingType.getName(), actual.getName());
    }

    @Test
    void testConvertToNewEntity() {
        BillingType actual = sut.convertToNewEntity(billingTypePojo);
        assertEquals(billingTypePojo.getName(), actual.getName());
    }
}
