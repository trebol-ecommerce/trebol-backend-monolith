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
import org.trebol.testhelpers.PeopleTestHelper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeopleJpaCrudServiceImplTest {
  @InjectMocks PeopleJpaCrudServiceImpl instance;
  @Mock IPeopleJpaRepository peopleRepositoryMock;
  PeopleTestHelper peopleHelper = new PeopleTestHelper();

  @BeforeEach
  void beforeEach() {
    peopleHelper.resetPeople();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    PersonPojo input = peopleHelper.personPojoForFetch();
    Person expectedResult = peopleHelper.personEntityAfterCreation();
    when(peopleRepositoryMock.findByIdNumber(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Person> match = instance.getExisting(input);

    verify(peopleRepositoryMock).findByIdNumber(input.getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
