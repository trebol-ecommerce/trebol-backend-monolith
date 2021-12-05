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
    String salespersonIdNumber = "11111111";
    String salespersonFirstName = "test first name";
    String salespersonLastName = "test last name";

    SalespersonPojo example = new SalespersonPojo(salespersonIdNumber);
    Salesperson persistedEntity = new Salesperson(salespersonIdNumber);
    persistedEntity.getPerson().setFirstName(salespersonFirstName);
    persistedEntity.getPerson().setLastName(salespersonLastName);
    when(salespeopleRepositoryMock.findByPersonIdNumber(salespersonIdNumber)).thenReturn(Optional.of(persistedEntity));

    SalespeopleJpaCrudServiceImpl service = instantiate();
    Optional<Salesperson> match = service.getExisting(example);

    assertTrue(match.isPresent());
    Person person = match.get().getPerson();
    assertEquals(person.getIdNumber(), salespersonIdNumber);
    assertEquals(person.getFirstName(), salespersonFirstName);
    assertEquals(person.getLastName(), salespersonLastName);
  }

  private SalespeopleJpaCrudServiceImpl instantiate() {
    return new SalespeopleJpaCrudServiceImpl(
        salespeopleRepositoryMock,
        salespeopleConverterMock
    );
  }

}
