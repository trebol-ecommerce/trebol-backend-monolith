package org.trebol.jpa.services.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.CustomersRepository;
import org.trebol.pojo.CustomerPojo;
import org.trebol.testhelpers.CustomersTestHelper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomersCrudServiceImplTest {
  @InjectMocks CustomersCrudServiceImpl instance;
  @Mock CustomersRepository customersRepositoryMock;
  CustomersTestHelper customersHelper = new CustomersTestHelper();

  @BeforeEach
  void beforeEach() {
    customersHelper.resetCustomers();
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    Customer expectedResult = customersHelper.customerEntityAfterCreation();
    CustomerPojo customer = customersHelper.customerPojoForFetch();
    when(customersRepositoryMock.findByPersonIdNumber(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Customer> match = instance.getExisting(customer);

    verify(customersRepositoryMock).findByPersonIdNumber(customer.getPerson().getIdNumber());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
