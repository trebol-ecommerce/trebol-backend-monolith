package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.pojo.BillingTypePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingTypesConverterJpaServiceImplTest {

    @InjectMocks
    private BillingTypesConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

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
    @Test
    void testConvertToPojo() {
        when(conversionService.convert(any(BillingType.class), eq(BillingTypePojo.class))).thenReturn(billingTypePojo);
        BillingTypePojo actual = sut.convertToPojo(billingType);
        assertEquals(ANY, actual.getName());
        verify(conversionService, times(1)).convert(any(BillingType.class), eq(BillingTypePojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        BillingType actual = sut.convertToNewEntity(billingTypePojo);
        assertEquals(ANY, actual.getName());
    }
}
