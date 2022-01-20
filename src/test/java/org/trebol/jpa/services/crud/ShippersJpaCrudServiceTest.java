package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ShipperPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShippersJpaCrudServiceTest {

  @Mock IShippersJpaRepository shippersRepositoryMock;
  @Mock ITwoWayConverterJpaService<ShipperPojo, Shipper> shippersConverterMock;

  @Test
  void sanity_check() {
    ShippersJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long shipperId = 1L;
    String shipperName = "test-one";
    ShipperPojo example = new ShipperPojo(shipperName);
    Shipper persistedEntity = new Shipper(shipperId, shipperName);
    when(shippersRepositoryMock.findByName(shipperName)).thenReturn(Optional.of(persistedEntity));
    ShippersJpaCrudServiceImpl service = instantiate();

    Optional<Shipper> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), shipperId);
    assertEquals(match.get().getName(), shipperName);
  }

  private ShippersJpaCrudServiceImpl instantiate() {
    return new ShippersJpaCrudServiceImpl(
        shippersRepositoryMock,
        shippersConverterMock
    );
  }

}
