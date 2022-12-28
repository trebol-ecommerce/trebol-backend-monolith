package org.trebol.jpa.services.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.pojo.CustomerPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.CustomersTestHelper.*;

@ExtendWith(MockitoExtension.class)
class CustomersJpaCrudServiceImplTest {
  @InjectMocks CustomersJpaCrudServiceImpl instance;
  @Mock ICustomersJpaRepository customersRepositoryMock;

  @BeforeEach
  void beforeEach() {
    resetCustomers();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    Customer expectedResult = customerEntityAfterCreation();
    CustomerPojo customer = customerPojoForFetch();
    when(customersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Customer> match = instance.getExisting(customer);

    verify(customersRepositoryMock).findByPersonIdNumber(customer.getPerson().getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
