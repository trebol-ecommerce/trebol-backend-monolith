package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.pojo.PersonPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.PeopleTestHelper.*;

@ExtendWith(MockitoExtension.class)
class PeopleJpaCrudServiceImplTest {
  @InjectMocks PeopleJpaCrudServiceImpl instance;
  @Mock IPeopleJpaRepository peopleRepositoryMock;

  @BeforeEach
  void beforeEach() {
    resetPeople();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    PersonPojo input = personPojoForFetch();
    Person expectedResult = personEntityAfterCreation();
    when(peopleRepositoryMock.findByIdNumber(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Person> match = instance.getExisting(input);

    verify(peopleRepositoryMock).findByIdNumber(input.getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
