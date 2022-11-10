package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.pojo.ShipperPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippersJpaCrudServiceImplTest {
  @InjectMocks ShippersJpaCrudServiceImpl instance;
  @Mock IShippersJpaRepository shippersRepositoryMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long shipperId = 1L;
    String shipperName = "test-one";
    Shipper persistedEntity = new Shipper(shipperId, shipperName);
    when(shippersRepositoryMock.findByName(shipperName)).thenReturn(Optional.of(persistedEntity));

    Optional<Shipper> match = instance.getExisting(ShipperPojo.builder().name(shipperName).build());

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), shipperId);
    assertEquals(match.get().getName(), shipperName);
  }

}
