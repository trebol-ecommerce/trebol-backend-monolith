package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SalespersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.trebol.jpa.testhelpers.SalespeopleJpaCrudServiceTestHelper.*;

@RunWith(MockitoJUnitRunner.class)
public class SalespeopleJpaCrudServiceTest {

  @Mock ISalespeopleJpaRepository salespeopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverterMock;

  @Test
  public void sanity_check() {
    SalespeopleJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_id_number() throws BadInputException {
    resetSalespeople();
    when(salespeopleRepositoryMock.findByPersonIdNumber(salespersonPojoForFetch().getPerson().getIdNumber())).thenReturn(Optional.of(salespersonEntityAfterCreation()));
    SalespeopleJpaCrudServiceImpl service = instantiate();

    Optional<Salesperson> match = service.getExisting(salespersonPojoForFetch());

    assertTrue(match.isPresent());
    Person person = match.get().getPerson();
    assertEquals(person.getIdNumber(), salespersonEntityAfterCreation().getPerson().getIdNumber());
    assertEquals(person.getFirstName(), salespersonEntityAfterCreation().getPerson().getFirstName());
    assertEquals(person.getLastName(), salespersonEntityAfterCreation().getPerson().getLastName());
    assertEquals(person.getEmail(), salespersonEntityAfterCreation().getPerson().getEmail());
  }

  private SalespeopleJpaCrudServiceImpl instantiate() {
    return new SalespeopleJpaCrudServiceImpl(
        salespeopleRepositoryMock,
        salespeopleConverterMock
    );
  }

}
