package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingTypePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingTypesJpaCrudServiceTest {
  @InjectMocks GenericCrudJpaService<BillingTypePojo, BillingType> instance;
  @Mock IBillingTypesJpaRepository billingTypesRepositoryMock;
  @Mock ITwoWayConverterJpaService<BillingTypePojo, BillingType> billingTypesConverterMock;
  @Mock IDataTransportJpaService<BillingTypePojo, BillingType> dataTransportServiceMock;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_name() throws BadInputException {
    Long billingTypeId = 1L;
    String billingTypeName = "test company";
    BillingTypePojo example = BillingTypePojo.builder().name(billingTypeName).build();
    BillingType persistedEntity = new BillingType(billingTypeId, billingTypeName);
    when(billingTypesRepositoryMock.findByName(billingTypeName)).thenReturn(Optional.of(persistedEntity));

    Optional<BillingType> match = instance.getExisting(example);

    verify(billingTypesRepositoryMock).findByName(billingTypeName);
    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), billingTypeId);
    assertEquals(match.get().getName(), billingTypeName);
  }
}
