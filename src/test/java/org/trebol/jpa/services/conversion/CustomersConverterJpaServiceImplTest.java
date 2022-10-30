package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class CustomersConverterJpaServiceImplTest {

    @InjectMocks
    private CustomersConverterJpaServiceImpl sut;

    @Mock
    private ITwoWayConverterJpaService<PersonPojo, Person> peopleService;

    private Customer customer;
    private CustomerPojo customerPojo;
    private Person person;
    private PersonPojo personPojo;


    @BeforeEach
    void beforeEach() {
        personPojo = PersonPojo.builder().id(ID_1L).build();
        person = new Person();
        person.setId(ID_1L);
        customer = new Customer();
        customer.setId(ID_1L);
        customer.setPerson(person);

        customerPojo = CustomerPojo.builder().person(personPojo).build();
    }


    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        when(peopleService.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(person);
        Customer actual = sut.applyChangesToExistingEntity(customerPojo, customer);
        assertEquals(person.getId(), actual.getPerson().getId());
    }

    @Test
    void testConvertToPojo() {
        when(peopleService.convertToPojo(any(Person.class))).thenReturn(personPojo);
        CustomerPojo actual = sut.convertToPojo(customer);
        assertEquals(personPojo.getId(), actual.getPerson().getId());
    }

    @Test
    void testConvertToNewEntity() throws BadInputException {
        when(peopleService.convertToNewEntity(any(PersonPojo.class))).thenReturn(person);
        Customer actual = sut.convertToNewEntity(customerPojo);
        assertEquals(person.getId(), actual.getPerson().getId());

    }
}
