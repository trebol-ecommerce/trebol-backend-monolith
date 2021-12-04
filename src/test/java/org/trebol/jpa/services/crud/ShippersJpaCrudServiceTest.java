package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.repositories.IShippersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ShipperPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ShippersJpaCrudServiceTest {

  @Mock IShippersJpaRepository shippersRepositoryMock;
  @Mock ITwoWayConverterJpaService<ShipperPojo, Shipper> shippersConverterMock;

  @Test
  public void sanity_check() {
    ShippersJpaCrudServiceImpl service = new ShippersJpaCrudServiceImpl(
        shippersRepositoryMock,
        shippersConverterMock
    );
    assertNotNull(service);
  }

}
