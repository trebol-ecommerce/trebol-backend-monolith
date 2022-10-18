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
import org.trebol.jpa.entities.SellStatus;
import org.trebol.pojo.SellStatusPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class SellStatusesConverterJpaServiceImplTest {
    @InjectMocks
    private SellStatusesConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    private SellStatus sellStatus;
    private SellStatusPojo sellStatusPojo;

    @BeforeEach
    void beforeEach() {
        sellStatus = new SellStatus();
        sellStatus.setName(ANY);
        sellStatus.setId(1L);
        sellStatus.setName(ANY);

        sellStatusPojo = SellStatusPojo.builder().name(ANY).build();
    }

    @AfterEach
    void afterEach() {
        sellStatus = null;
        sellStatusPojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        sellStatusPojo.setName("PIOLO");
        SellStatus actual = sut.applyChangesToExistingEntity(sellStatusPojo, sellStatus);
        assertEquals(1L, actual.getId());
    }
    @Test
    void testConvertToPojo() {
        when(conversionService.convert(any(SellStatus.class), eq(SellStatusPojo.class))).thenReturn(sellStatusPojo);
        SellStatusPojo actual = sut.convertToPojo(sellStatus);
        assertEquals(ANY, actual.getName());
        verify(conversionService, times(1)).convert(any(SellStatus.class), eq(SellStatusPojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        SellStatus actual = sut.convertToNewEntity(sellStatusPojo);
        assertEquals(ANY, actual.getName());
    }
}
