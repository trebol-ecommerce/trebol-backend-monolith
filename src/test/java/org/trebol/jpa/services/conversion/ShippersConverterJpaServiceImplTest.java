package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Shipper;
import org.trebol.pojo.ShipperPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ShippersConverterJpaServiceImplTest {
    @InjectMocks ShippersConverterJpaServiceImpl sut;
    Shipper shipper;
    ShipperPojo shipperPojo;

    @BeforeEach
    void beforeEach() {
        shipper = new Shipper();
        shipper.setName(ANY);
        shipper.setId(1L);
        shipper.setName(ANY);

        shipperPojo = ShipperPojo.builder().name(ANY).build();
    }

    @AfterEach
    void afterEach() {
        shipper = null;
        shipperPojo = null;
    }

    @Test
    void testConvertToPojo() {
        ShipperPojo actual = sut.convertToPojo(shipper);
        assertEquals(shipper.getName(), actual.getName());
    }

    @Test
    void testConvertToNewEntity() {
        Shipper actual = sut.convertToNewEntity(shipperPojo);
        assertEquals(shipperPojo.getName(), actual.getName());
    }
}
