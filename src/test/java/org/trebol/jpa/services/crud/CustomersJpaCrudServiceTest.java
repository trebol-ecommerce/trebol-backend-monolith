package org.trebol.jpa.services.crud;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.CustomersTestHelper.*;

@ExtendWith(MockitoExtension.class)
class CustomersJpaCrudServiceTest {
  @InjectMocks GenericCrudJpaService<CustomerPojo, Customer> instance;
  @Mock ICustomersJpaRepository customersRepositoryMock;
  @Mock ITwoWayConverterJpaService<CustomerPojo, Customer> billingTypesConverterMock;
  @Mock IDataTransportJpaService<CustomerPojo, Customer> dataTransportServiceMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    CustomerPojo customer = customerPojoForFetch();
    String idNumber = customer.getPerson().getIdNumber();
    resetCustomers();
    when(customersRepositoryMock.findByPersonIdNumber(idNumber)).thenReturn(Optional.of(customerEntityAfterCreation()));

    Optional<Customer> match = instance.getExisting(customer);

    verify(customersRepositoryMock).findByPersonIdNumber(idNumber);
    assertTrue(match.isPresent());
    Person person = match.get().getPerson();
    assertEquals(person.getIdNumber(), customerEntityAfterCreation().getPerson().getIdNumber());
    assertEquals(person.getFirstName(), customerEntityAfterCreation().getPerson().getFirstName());
    assertEquals(person.getLastName(), customerEntityAfterCreation().getPerson().getLastName());
  }
}
