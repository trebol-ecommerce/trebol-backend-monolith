package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.PeopleTestHelper.*;

@ExtendWith(MockitoExtension.class)
class PeopleJpaCrudServiceTest {
  @InjectMocks GenericCrudJpaService<PersonPojo, Person> instance;
  @Mock IPeopleJpaRepository peopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<PersonPojo, Person> peopleConverterMock;
  @Mock IDataTransportJpaService<PersonPojo, Person> dataTransportServiceMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    resetPeople();
    when(peopleRepositoryMock.findByIdNumber(personPojoForFetch().getIdNumber())).thenReturn(Optional.of(personEntityAfterCreation()));

    Optional<Person> match = instance.getExisting(personPojoForFetch());

    assertTrue(match.isPresent());
    assertEquals(match.get().getIdNumber(), personEntityAfterCreation().getIdNumber());
    assertEquals(match.get().getFirstName(), personEntityAfterCreation().getFirstName());
    assertEquals(match.get().getLastName(), personEntityAfterCreation().getLastName());
    assertEquals(match.get().getEmail(), personEntityAfterCreation().getEmail());
  }
}
