package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomersJpaCrudServiceTest {

  @Mock ICustomersJpaRepository customersRepositoryMock;
  @Mock ITwoWayConverterJpaService<CustomerPojo, Customer> billingTypesConverterMock;

  @Test
  public void sanity_check() {
    CustomersJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_id_number() throws BadInputException {
    String customerIdNumber = "11111111";
    String customerFirstName = "test first name";
    String customerLastName = "test last name";
    CustomerPojo example = new CustomerPojo(customerIdNumber);
    Customer persistedEntity = new Customer(customerIdNumber);
    persistedEntity.getPerson().setFirstName(customerFirstName);
    persistedEntity.getPerson().setLastName(customerLastName);
    when(customersRepositoryMock.findByPersonIdNumber(customerIdNumber)).thenReturn(Optional.of(persistedEntity));
    CustomersJpaCrudServiceImpl service = instantiate();

    Optional<Customer> match = service.getExisting(example);

    assertTrue(match.isPresent());
    Person person = match.get().getPerson();
    assertEquals(person.getIdNumber(), customerIdNumber);
    assertEquals(person.getFirstName(), customerFirstName);
    assertEquals(person.getLastName(), customerLastName);
  }

  private CustomersJpaCrudServiceImpl instantiate() {
    return new CustomersJpaCrudServiceImpl(
        customersRepositoryMock,
        billingTypesConverterMock
    );
  }

}
