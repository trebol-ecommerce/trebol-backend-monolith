package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.SalespeopleJpaRepository;
import org.trebol.pojo.SalespersonPojo;
import org.trebol.testhelpers.SalespeopleTestHelper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalespeopleCrudServiceImplTest {
  @InjectMocks SalespeopleCrudServiceImpl instance;
  @Mock SalespeopleJpaRepository salespeopleRepositoryMock;
  SalespeopleTestHelper salespeopleHelper = new SalespeopleTestHelper();

  @BeforeEach
  void beforeEach() {
    salespeopleHelper.resetSalespeople();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    SalespersonPojo input = salespeopleHelper.salespersonPojoForFetch();
    Salesperson expectedResult = salespeopleHelper.salespersonEntityAfterCreation();
    when(salespeopleRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(salespeopleHelper.salespersonEntityAfterCreation()));

    Optional<Salesperson> match = instance.getExisting(input);

    verify(salespeopleRepositoryMock).findByPersonIdNumber(input.getPerson().getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
