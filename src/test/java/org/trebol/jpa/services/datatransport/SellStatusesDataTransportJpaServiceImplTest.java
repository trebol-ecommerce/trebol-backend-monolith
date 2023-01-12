package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.pojo.SellStatusPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class SellStatusesDataTransportJpaServiceImplTest {
  @InjectMocks SellStatusesDataTransportJpaServiceImpl sut;
  SellStatus sellStatus;
  SellStatusPojo sellStatusPojo;

  @BeforeEach
  void beforeEach() {
    sellStatus = new SellStatus();
    sellStatus.setName(ANY);
    sellStatus.setId(1L);
    sellStatus.setName(ANY);
    sellStatusPojo = SellStatusPojo.builder().name(ANY).build();
  }

  @AfterEach
  void afterEach() {
    sellStatus = null;
    sellStatusPojo = null;
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    sellStatusPojo.setName("PIOLO");
    SellStatus actual = sut.applyChangesToExistingEntity(sellStatusPojo, sellStatus);
    assertEquals(1L, actual.getId());
  }
}
