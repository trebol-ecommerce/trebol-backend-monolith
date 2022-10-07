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
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class SalespeopleConverterJpaServiceImplTest {

    @InjectMocks
    private SalespeopleConverterJpaServiceImpl sut;

    @Mock
    private ITwoWayConverterJpaService<PersonPojo, Person> peopleService;

    private Salesperson salesperson;
    private SalespersonPojo salespersonPojo;
    private Person person;
    private PersonPojo personPojo;


    @BeforeEach
    void beforeEach() {
        personPojo = new PersonPojo();
        personPojo.setId(ID_1L);
        person = new Person();
        person.setId(ID_1L);
        salesperson = new Salesperson();
        salesperson.setId(ID_1L);
        salesperson.setPerson(person);

        salespersonPojo = new SalespersonPojo();
        salespersonPojo.setPerson(personPojo);
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
        when(peopleService.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(person);
        Salesperson actual = sut.applyChangesToExistingEntity(salespersonPojo, salesperson);
        assertEquals(person.getId(), actual.getPerson().getId());
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
