package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SellStatusPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SellStatusesJpaCrudServiceTest {

  @Mock ISellStatusesJpaRepository sellStatusesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellStatusPojo, SellStatus> sellStatusesConverterMock;

  @Test
  public void sanity_check() {
    SellStatusesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_name() throws BadInputException {
    Long statusId = 1L;
    Integer statusCode = 0;
    String statusName = "example sell status name";
    SellStatusPojo example = new SellStatusPojo(statusName);
    SellStatus persistedEntity = new SellStatus(statusId, statusCode, statusName);
    when(sellStatusesRepositoryMock.findByName(statusName)).thenReturn(Optional.of(persistedEntity));

    SellStatusesJpaCrudServiceImpl service = instantiate();
    Optional<SellStatus> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), statusId);
    assertEquals(match.get().getCode(), statusCode);
    assertEquals(match.get().getName(), statusName);
  }

  private SellStatusesJpaCrudServiceImpl instantiate() {
    return new SellStatusesJpaCrudServiceImpl(
        sellStatusesRepositoryMock,
        sellStatusesConverterMock
    );
  }

}
