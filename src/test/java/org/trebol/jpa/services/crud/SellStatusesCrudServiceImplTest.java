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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellStatusesCrudServiceImplTest {
  @InjectMocks SellStatusesCrudServiceImpl instance;
  @Mock ISellStatusesJpaRepository sellStatusesRepositoryMock;

  @Test
  void finds_by_name() throws BadInputException {
    String statusName = "example sell status name";
    SellStatusPojo input = SellStatusPojo.builder()
      .name(statusName)
      .build();
    SellStatus expectedResult = new SellStatus(1L, 0, statusName);
    when(sellStatusesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<SellStatus> match = instance.getExisting(input);

    verify(sellStatusesRepositoryMock).findByName(statusName);
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
