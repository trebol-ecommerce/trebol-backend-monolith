package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SellStatusPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SellStatusesJpaCrudServiceTest {

  @Mock ISellStatusesJpaRepository sellStatusesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellStatusPojo, SellStatus> sellStatusesConverterMock;

  @Test
  public void sanity_check() {
    SellStatusesJpaCrudServiceImpl service = new SellStatusesJpaCrudServiceImpl(
        sellStatusesRepositoryMock,
        sellStatusesConverterMock
    );
    assertNotNull(service);
  }

}
