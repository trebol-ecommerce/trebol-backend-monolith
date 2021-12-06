package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleJpaCrudServiceTest {

  @Mock IPeopleJpaRepository peopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<PersonPojo, Person> peopleConverterMock;

  @Test
  public void sanity_check() {
    PeopleJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_id_number() throws BadInputException {
    String personIdNumber = "11111111";
    String personFirstName = "test first name";
    String personLastName = "test last name";
    PersonPojo example = new PersonPojo(personIdNumber);
    Person persistedEntity = new Person(personFirstName, personLastName, personIdNumber);
    when(peopleRepositoryMock.findByIdNumber(personIdNumber)).thenReturn(Optional.of(persistedEntity));
    PeopleJpaCrudServiceImpl service = instantiate();

    Optional<Person> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getIdNumber(), personIdNumber);
    assertEquals(match.get().getFirstName(), personFirstName);
    assertEquals(match.get().getLastName(), personLastName);
  }

  private PeopleJpaCrudServiceImpl instantiate() {
    return new PeopleJpaCrudServiceImpl(
        peopleRepositoryMock,
        peopleConverterMock
    );
  }

}
