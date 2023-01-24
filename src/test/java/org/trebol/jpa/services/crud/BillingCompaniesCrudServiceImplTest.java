package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.repositories.BillingCompaniesJpaRepository;
import org.trebol.pojo.BillingCompanyPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingCompaniesCrudServiceImplTest {
  @InjectMocks BillingCompaniesCrudServiceImpl instance;
  @Mock BillingCompaniesJpaRepository billingCompaniesRepositoryMock;

  @Test
  void finds_by_id_number() throws BadInputException {
    String companyIdNumber = "11111111";
    BillingCompanyPojo input = BillingCompanyPojo.builder()
      .idNumber(companyIdNumber)
      .build();
    BillingCompany expectedResult = new BillingCompany(1L, companyIdNumber, "test company");
    when(billingCompaniesRepositoryMock.findByIdNumber(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<BillingCompany> match = instance.getExisting(input);

    verify(billingCompaniesRepositoryMock).findByIdNumber(companyIdNumber);
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
