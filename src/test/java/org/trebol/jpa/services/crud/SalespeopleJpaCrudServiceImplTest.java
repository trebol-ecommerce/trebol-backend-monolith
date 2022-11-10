package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.SalespeopleTestHelper.*;

@ExtendWith(MockitoExtension.class)
class SalespeopleJpaCrudServiceImplTest {
  @InjectMocks SalespeopleJpaCrudServiceImpl instance;
  @Mock ISalespeopleJpaRepository salespeopleRepositoryMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    resetSalespeople();
    when(salespeopleRepositoryMock.findByPersonIdNumber(salespersonPojoForFetch().getPerson().getIdNumber())).thenReturn(Optional.of(salespersonEntityAfterCreation()));

    Optional<Salesperson> match = instance.getExisting(salespersonPojoForFetch());

    assertTrue(match.isPresent());
    Person person = match.get().getPerson();
    assertEquals(person.getIdNumber(), salespersonEntityAfterCreation().getPerson().getIdNumber());
    assertEquals(person.getFirstName(), salespersonEntityAfterCreation().getPerson().getFirstName());
    assertEquals(person.getLastName(), salespersonEntityAfterCreation().getPerson().getLastName());
    assertEquals(person.getEmail(), salespersonEntityAfterCreation().getPerson().getEmail());
  }
}
