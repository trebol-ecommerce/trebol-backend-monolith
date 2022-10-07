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
import org.trebol.jpa.entities.Shipper;
import org.trebol.pojo.ShipperPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ShippersConverterJpaServiceImplTest {
    @InjectMocks
    private ShippersConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    private Shipper shipper;
    private ShipperPojo shipperPojo;

    @BeforeEach
    void beforeEach() {
        shipper = new Shipper();
        shipper.setName(ANY);
        shipper.setId(1L);
        shipper.setName(ANY);

        shipperPojo = new ShipperPojo();
        shipperPojo.setName(ANY);
    }

    @AfterEach
    void afterEach() {
        shipper = null;
        shipperPojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        shipperPojo.setName("PIOLO");
        Shipper actual = sut.applyChangesToExistingEntity(shipperPojo, shipper);
        assertEquals(1L, actual.getId());
    }
    @Test
    void testConvertToPojo() {
        when(conversionService.convert(any(Shipper.class), eq(ShipperPojo.class))).thenReturn(shipperPojo);
        ShipperPojo actual = sut.convertToPojo(shipper);
        assertEquals(ANY, actual.getName());
        verify(conversionService, times(1)).convert(any(Shipper.class), eq(ShipperPojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        Shipper actual = sut.convertToNewEntity(shipperPojo);
        assertEquals(ANY, actual.getName());
    }
}
