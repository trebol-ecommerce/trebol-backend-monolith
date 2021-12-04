package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingCompanyPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BillingCompaniesJpaCrudServiceTest {

  @Mock IBillingCompaniesJpaRepository billingCompaniesRepositoryMock;
  @Mock ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverterMock;

  @Test
  public void sanity_check() {
    BillingCompaniesJpaCrudServiceImpl service = new BillingCompaniesJpaCrudServiceImpl(
        billingCompaniesRepositoryMock,
        billingCompaniesConverterMock
    );
    assertNotNull(service);
  }

}
