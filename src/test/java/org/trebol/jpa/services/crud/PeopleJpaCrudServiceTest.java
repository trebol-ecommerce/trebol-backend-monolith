package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.PeopleTestHelper.*;

@ExtendWith(MockitoExtension.class)
class PeopleJpaCrudServiceTest {

  @Mock IPeopleJpaRepository peopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<PersonPojo, Person> peopleConverterMock;

  @Test
  void sanity_check() {
    PeopleJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    resetPeople();
    when(peopleRepositoryMock.findByIdNumber(personPojoForFetch().getIdNumber())).thenReturn(Optional.of(personEntityAfterCreation()));
    PeopleJpaCrudServiceImpl service = instantiate();

    Optional<Person> match = service.getExisting(personPojoForFetch());

    assertTrue(match.isPresent());
    assertEquals(match.get().getIdNumber(), personEntityAfterCreation().getIdNumber());
    assertEquals(match.get().getFirstName(), personEntityAfterCreation().getFirstName());
    assertEquals(match.get().getLastName(), personEntityAfterCreation().getLastName());
    assertEquals(match.get().getEmail(), personEntityAfterCreation().getEmail());
  }

  private PeopleJpaCrudServiceImpl instantiate() {
    return new PeopleJpaCrudServiceImpl(
        peopleRepositoryMock,
        peopleConverterMock
    );
  }

}
