package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CustomersJpaCrudServiceTest {

  @Mock ICustomersJpaRepository customersRepositoryMock;
  @Mock ITwoWayConverterJpaService<CustomerPojo, Customer> billingTypesConverterMock;

  @Test
  public void sanity_check() {
    CustomersJpaCrudServiceImpl service = new CustomersJpaCrudServiceImpl(
        customersRepositoryMock,
        billingTypesConverterMock
    );
    assertNotNull(service);
  }

}
