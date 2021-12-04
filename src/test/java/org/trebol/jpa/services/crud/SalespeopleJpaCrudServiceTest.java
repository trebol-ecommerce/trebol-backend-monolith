package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.repositories.ISalespeopleJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.SalespersonPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SalespeopleJpaCrudServiceTest {

  @Mock ISalespeopleJpaRepository salespeopleRepositoryMock;
  @Mock ITwoWayConverterJpaService<SalespersonPojo, Salesperson> salespeopleConverterMock;

  @Test
  public void sanity_check() {
    SalespeopleJpaCrudServiceImpl service = new SalespeopleJpaCrudServiceImpl(
        salespeopleRepositoryMock,
        salespeopleConverterMock
    );
    assertNotNull(service);
  }

}
