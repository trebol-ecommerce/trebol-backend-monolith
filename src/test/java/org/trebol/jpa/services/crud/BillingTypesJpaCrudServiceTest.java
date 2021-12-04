package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingTypePojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BillingTypesJpaCrudServiceTest {

  @Mock IBillingTypesJpaRepository billingTypesRepositoryMock;
  @Mock ITwoWayConverterJpaService<BillingTypePojo, BillingType> billingTypesConverterMock;

  @Test
  public void sanity_check() {
    BillingTypesJpaCrudServiceImpl service = new BillingTypesJpaCrudServiceImpl(
        billingTypesRepositoryMock,
        billingTypesConverterMock
    );
    assertNotNull(service);
  }

}
