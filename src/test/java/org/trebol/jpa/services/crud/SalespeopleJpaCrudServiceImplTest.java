package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;
import org.trebol.pojo.SalespersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.SalespeopleTestHelper.*;

@ExtendWith(MockitoExtension.class)
class SalespeopleJpaCrudServiceImplTest {
  @InjectMocks SalespeopleJpaCrudServiceImpl instance;
  @Mock ISalespeopleJpaRepository salespeopleRepositoryMock;

  @BeforeEach
  void beforeEach() {
    resetSalespeople();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    SalespersonPojo input = salespersonPojoForFetch();
    Salesperson expectedResult = salespersonEntityAfterCreation();
    when(salespeopleRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(salespersonEntityAfterCreation()));

    Optional<Salesperson> match = instance.getExisting(input);

    verify(salespeopleRepositoryMock).findByPersonIdNumber(input.getPerson().getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
