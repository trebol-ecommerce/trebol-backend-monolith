package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.IBillingTypesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.BillingTypePojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BillingTypesJpaCrudServiceTest {

  @Mock IBillingTypesJpaRepository billingTypesRepositoryMock;
  @Mock ITwoWayConverterJpaService<BillingTypePojo, BillingType> billingTypesConverterMock;

  @Test
  public void sanity_check() {
    BillingTypesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_name() throws BadInputException {
    Long billingTypeId = 1L;
    String billingTypeName = "test company";
    BillingTypePojo example = new BillingTypePojo(billingTypeName);
    BillingType persistedEntity = new BillingType(billingTypeId, billingTypeName);
    when(billingTypesRepositoryMock.findByName(billingTypeName)).thenReturn(Optional.of(persistedEntity));

    BillingTypesJpaCrudServiceImpl service = instantiate();
    Optional<BillingType> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get(), persistedEntity);
  }

  private BillingTypesJpaCrudServiceImpl instantiate() {
    return new BillingTypesJpaCrudServiceImpl(
        billingTypesRepositoryMock,
        billingTypesConverterMock
    );
  }
  
}
