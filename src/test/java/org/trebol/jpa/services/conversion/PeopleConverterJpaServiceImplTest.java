package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

        personPojo = PersonPojo.builder().id(1L).build();
    }

    @AfterEach
    void afterEach() {
        person = null;
        personPojo = null;
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
