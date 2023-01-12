package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class SalespeopleConverterJpaServiceImplTest {
  @InjectMocks SalespeopleConverterJpaServiceImpl sut;
  @Mock IPeopleConverterJpaService peopleService;
  Salesperson salesperson;
  SalespersonPojo salespersonPojo;
  Person person;
  PersonPojo personPojo;

  @BeforeEach
  void beforeEach() {
    personPojo = PersonPojo.builder()
      .id(ID_1L)
      .build();
    person = new Person();
    person.setId(ID_1L);
    salesperson = new Salesperson();
    salesperson.setId(ID_1L);
    salesperson.setPerson(person);
    salespersonPojo = SalespersonPojo.builder()
      .person(personPojo)
      .build();
  }

  @Test
  void testConvertToPojo() {
    when(peopleService.convertToPojo(any(Person.class))).thenReturn(personPojo);
    SalespersonPojo actual = sut.convertToPojo(salesperson);
    assertEquals(personPojo.getId(), actual.getPerson().getId());
  }

  @Test
  void testConvertToNewEntity() throws BadInputException {
    when(peopleService.convertToNewEntity(any(PersonPojo.class))).thenReturn(person);
    Salesperson actual = sut.convertToNewEntity(salespersonPojo);
    assertEquals(person.getId(), actual.getPerson().getId());
  }
}
