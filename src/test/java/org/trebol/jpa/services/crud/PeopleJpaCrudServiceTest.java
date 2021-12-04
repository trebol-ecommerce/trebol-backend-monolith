package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PeopleJpaCrudServiceTest {

  @Mock IPeopleJpaRepository peopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<PersonPojo, Person> peopleConverterMock;

  @Test
  public void sanity_check() {
    PeopleJpaCrudServiceImpl service = new PeopleJpaCrudServiceImpl(
        peopleRepositoryMock,
        peopleConverterMock
    );
    assertNotNull(service);
  }

}
