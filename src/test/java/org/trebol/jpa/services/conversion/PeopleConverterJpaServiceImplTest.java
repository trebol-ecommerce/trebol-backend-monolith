package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

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
        PersonPojo actual = sut.convertToPojo(person);
        Assertions.assertEquals(person.getIdNumber(), actual.getIdNumber());
    }

    @Test
    void testConvertToNewEntity() {
        Person actual = sut.convertToNewEntity(personPojo);
    }
}
