package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.pojo.SellStatusPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellStatusesJpaCrudServiceImplTest {
  @InjectMocks SellStatusesJpaCrudServiceImpl instance;
  @Mock ISellStatusesJpaRepository sellStatusesRepositoryMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long statusId = 1L;
    Integer statusCode = 0;
    String statusName = "example sell status name";
    SellStatus persistedEntity = new SellStatus(statusId, statusCode, statusName);
    when(sellStatusesRepositoryMock.findByName(statusName)).thenReturn(Optional.of(persistedEntity));

    Optional<SellStatus> match = instance.getExisting(SellStatusPojo.builder().name(statusName).build());

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), statusId);
    assertEquals(match.get().getCode(), statusCode);
    assertEquals(match.get().getName(), statusName);
  }
}
