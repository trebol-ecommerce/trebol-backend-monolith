package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SellStatusPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellStatusesJpaCrudServiceTest {
  @Mock ISellStatusesJpaRepository sellStatusesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellStatusPojo, SellStatus> sellStatusesConverterMock;
  private GenericCrudJpaService<SellStatusPojo, SellStatus> instance;

  @BeforeEach
  void setUp() {
    instance = new SellStatusesJpaCrudServiceImpl(
            sellStatusesRepositoryMock,
            sellStatusesConverterMock
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long statusId = 1L;
    Integer statusCode = 0;
    String statusName = "example sell status name";
    SellStatusPojo example = new SellStatusPojo(statusName);
    SellStatus persistedEntity = new SellStatus(statusId, statusCode, statusName);
    when(sellStatusesRepositoryMock.findByName(statusName)).thenReturn(Optional.of(persistedEntity));

    Optional<SellStatus> match = instance.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), statusId);
    assertEquals(match.get().getCode(), statusCode);
    assertEquals(match.get().getName(), statusName);
  }
}
