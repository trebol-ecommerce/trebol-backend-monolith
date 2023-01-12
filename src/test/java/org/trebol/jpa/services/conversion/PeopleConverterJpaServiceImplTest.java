package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class PeopleConverterJpaServiceImplTest {
  @InjectMocks PeopleConverterJpaServiceImpl sut;
  Person person;
  PersonPojo personPojo;

  @BeforeEach
  void beforeEach() {
    person = new Person();
    person.setId(1L);
    personPojo = PersonPojo.builder().id(1L).build();
  }

  @AfterEach
  void afterEach() {
    person = null;
    personPojo = null;
  }

  @Test
  void testConvertToPojo() {
    person.setIdNumber(ANY);
    person.setFirstName(ANY);
    person.setLastName(ANY);
    person.setEmail(ANY);
    PersonPojo actual = sut.convertToPojo(person);
    assertEquals(person.getIdNumber(), actual.getIdNumber());
    assertEquals(person.getFirstName(), actual.getFirstName());
    assertEquals(person.getLastName(), actual.getLastName());
    assertEquals(person.getEmail(), actual.getEmail());
    assertEquals(person.getPhone1(), actual.getPhone1());
    assertEquals(person.getPhone2(), actual.getPhone2());
  }

  @Test
  void testConvertToNewEntity() {
    personPojo.setIdNumber(ANY);
    personPojo.setFirstName(ANY);
    personPojo.setLastName(ANY);
    personPojo.setEmail(ANY);
    personPojo.setPhone1("");
    personPojo.setPhone2("");
    Person actual = sut.convertToNewEntity(personPojo);
    assertEquals(personPojo.getIdNumber(), actual.getIdNumber());
    assertEquals(personPojo.getFirstName(), actual.getFirstName());
    assertEquals(personPojo.getLastName(), actual.getLastName());
    assertEquals(personPojo.getEmail(), actual.getEmail());
    assertEquals(personPojo.getPhone1(), actual.getPhone1());
    assertEquals(personPojo.getPhone2(), actual.getPhone2());
  }
}
