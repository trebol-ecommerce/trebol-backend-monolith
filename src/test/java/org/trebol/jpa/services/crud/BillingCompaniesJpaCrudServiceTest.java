package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.repositories.IBillingCompaniesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingCompanyPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillingCompaniesJpaCrudServiceTest {

  @Mock IBillingCompaniesJpaRepository billingCompaniesRepositoryMock;
  @Mock ITwoWayConverterJpaService<BillingCompanyPojo, BillingCompany> billingCompaniesConverterMock;

  @Test
  void sanity_check() {
    BillingCompaniesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  void finds_by_id_number() throws BadInputException {
    Long companyId = 1L;
    String companyIdNumber = "11111111";
    String companyName = "test company";
    BillingCompanyPojo example = new BillingCompanyPojo(companyIdNumber);
    BillingCompany persistedEntity = new BillingCompany(companyId, companyIdNumber, companyName);
    when(billingCompaniesRepositoryMock.findByIdNumber(companyIdNumber)).thenReturn(Optional.of(persistedEntity));
    BillingCompaniesJpaCrudServiceImpl service = instantiate();

    Optional<BillingCompany> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), companyId);
    assertEquals(match.get().getIdNumber(), companyIdNumber);
    assertEquals(match.get().getName(), companyName);
  }

  private BillingCompaniesJpaCrudServiceImpl instantiate() {
    return new BillingCompaniesJpaCrudServiceImpl(
        billingCompaniesRepositoryMock,
        billingCompaniesConverterMock
    );
  }

}
