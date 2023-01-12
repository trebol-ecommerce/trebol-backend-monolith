package org.trebol.jpa.services.datatransport;

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class SalespeopleDataTransportJpaServiceImplTest {
  @InjectMocks SalespeopleDataTransportJpaServiceImpl sut;
  @Mock IPeopleDataTransportJpaService peopleDataTransportService;
  Salesperson salesperson;
  SalespersonPojo salespersonPojo;
  Person person;

  @BeforeEach
  void beforeEach() {
    person = new Person();
    person.setId(ID_1L);
    salesperson = new Salesperson();
    salesperson.setId(ID_1L);
    salesperson.setPerson(person);
    salespersonPojo = SalespersonPojo.builder()
      .person(PersonPojo.builder().id(ID_1L).build())
      .build();
  }


  @Test
  void testApplyChangesToExistingEntityThrowsBadInputException() {
    salespersonPojo.setPerson(null);
    BadInputException badInputException = assertThrows(BadInputException.class, ()
      -> sut.applyChangesToExistingEntity(salespersonPojo, salesperson));
    assertEquals("Salesperson must have a person profile", badInputException.getMessage());
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    when(peopleDataTransportService.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(person);
    Salesperson actual = sut.applyChangesToExistingEntity(salespersonPojo, salesperson);
    assertEquals(person.getId(), actual.getPerson().getId());
  }
}
