package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.BillingTypesRepository;
import org.trebol.pojo.BillingTypePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingTypesCrudServiceImplTest {
  @InjectMocks BillingTypesCrudServiceImpl instance;
  @Mock BillingTypesRepository billingTypesRepositoryMock;

  @Test
  void finds_by_name() throws BadInputException {
    String billingTypeName = "test company";
    BillingTypePojo example = BillingTypePojo.builder()
      .name(billingTypeName)
      .build();
    BillingType expectedResult = new BillingType(1L, billingTypeName);
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<BillingType> match = instance.getExisting(example);

    verify(billingTypesRepositoryMock).findByName(billingTypeName);
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
