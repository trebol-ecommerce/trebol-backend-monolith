package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.pojo.BillingCompanyPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingCompaniesConverterJpaServiceImplTest {
  @InjectMocks
  private BillingCompaniesConverterJpaServiceImpl sut;

  @Mock
  private ConversionService conversionService;

  private BillingCompany billingCompany;
  private BillingCompanyPojo billingCompanyPojo;

  @BeforeEach
  void beforeEach() {
    billingCompany = new BillingCompany();
    billingCompany.setName(ANY);
    billingCompany.setId(1L);
    billingCompany.setIdNumber(ANY);

    billingCompanyPojo = new BillingCompanyPojo();
    billingCompanyPojo.setIdNumber(ANY);
    billingCompanyPojo.setName(ANY);
  }

  @AfterEach
  void afterEach() {
    billingCompany = null;
    billingCompanyPojo = null;
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    billingCompanyPojo.setName("PIOLO");
    BillingCompany actual = sut.applyChangesToExistingEntity(billingCompanyPojo, billingCompany);
    assertEquals(1L, actual.getId());
  }

  @Test
  void testConvertToPojo() {
    when(conversionService.convert(any(BillingCompany.class), eq(BillingCompanyPojo.class))).thenReturn(billingCompanyPojo);
    BillingCompanyPojo actual = sut.convertToPojo(billingCompany);
    assertEquals(ANY, actual.getIdNumber());
    verify(conversionService, times(1)).convert(any(BillingCompany.class), eq(BillingCompanyPojo.class));
  }

  @Test
  void testConvertToNewEntity() {
    BillingCompany actual = sut.convertToNewEntity(billingCompanyPojo);
    assertEquals(ANY, actual.getIdNumber());
  }
}
