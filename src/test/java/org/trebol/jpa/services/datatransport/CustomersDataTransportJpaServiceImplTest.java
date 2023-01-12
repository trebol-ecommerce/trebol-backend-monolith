package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class CustomersDataTransportJpaServiceImplTest {
  @InjectMocks CustomersDataTransportJpaServiceImpl sut;
  @Mock IPeopleDataTransportJpaService peopleService;
  Customer customer;
  Person person;
  CustomerPojo customerPojo;

  @BeforeEach
  void beforeEach() {
    person = new Person();
    person.setId(ID_1L);
    customer = new Customer();
    customer.setId(ID_1L);
    customer.setPerson(person);
    customerPojo = CustomerPojo.builder()
      .person(PersonPojo.builder().id(ID_1L).build())
      .build();
  }


  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    when(peopleService.applyChangesToExistingEntity(any(PersonPojo.class), any(Person.class))).thenReturn(person);
    Customer actual = sut.applyChangesToExistingEntity(customerPojo, customer);
    assertEquals(person, actual.getPerson());
  }
}
