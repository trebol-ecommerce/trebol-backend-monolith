package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class PeopleDataTransportJpaServiceImplTest {
    @InjectMocks PeopleDataTransportJpaServiceImpl sut;

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
}
