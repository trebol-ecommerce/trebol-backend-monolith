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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippersCrudServiceImplTest {
  @InjectMocks ShippersCrudServiceImpl instance;
  @Mock IShippersJpaRepository shippersRepositoryMock;

  @Test
  void finds_by_name() throws BadInputException {
    String shipperName = "test-one";
    ShipperPojo input = ShipperPojo.builder()
      .name(shipperName)
      .build();
    Shipper expectedResult = new Shipper(1L, shipperName);
    when(shippersRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Shipper> match = instance.getExisting(input);

    verify(shippersRepositoryMock).findByName(shipperName);
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

}
