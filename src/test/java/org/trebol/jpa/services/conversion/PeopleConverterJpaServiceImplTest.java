package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestContants.ANY;

@ExtendWith(MockitoExtension.class)
class PeopleConverterJpaServiceImplTest {
    @InjectMocks
    private PeopleConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    private Person person;
    private PersonPojo personPojo;

    @BeforeEach
    void beforeEach() {
        person = new Person();
        person.setId(1L);

        personPojo = new PersonPojo();
        personPojo.setId(1L);
    }

    @AfterEach
    void afterEach() {
        person = null;
        personPojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        Person actual = sut.applyChangesToExistingEntity(personPojo, person);
        assertEquals(1L, actual.getId());

        person.setEmail(ANY);
        person.setFirstName(ANY);
        person.setLastName(ANY);
        person.setIdNumber(ANY);
        person.setPhone1(ANY);
        person.setPhone2(ANY);


        personPojo.setEmail(ANY + " ");
        personPojo.setFirstName(ANY + " ");
        personPojo.setLastName(ANY + " ");
        personPojo.setIdNumber(ANY + " ");
        personPojo.setPhone1(ANY + " ");
        personPojo.setPhone2(ANY + " ");

        actual = sut.applyChangesToExistingEntity(personPojo, person);

        assertEquals(ANY + " ", actual.getEmail());
        assertEquals(ANY + " ", actual.getFirstName());
        assertEquals(ANY + " ", actual.getLastName());
        assertEquals(ANY + " ", actual.getPhone1());
        assertEquals(ANY + " ", actual.getPhone2());
    }
    @Test
    void testConvertToPojo() {
        when(conversionService.convert(any(Person.class), eq(PersonPojo.class))).thenReturn(personPojo);
        PersonPojo actual = sut.convertToPojo(person);
        verify(conversionService, times(1)).convert(any(Person.class), eq(PersonPojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        when(conversionService.convert(any(PersonPojo.class), eq(Person.class))).thenReturn(person);
        Person actual = sut.convertToNewEntity(personPojo);
        verify(conversionService, times(1)).convert(any(PersonPojo.class), eq(Person.class));
    }
}
