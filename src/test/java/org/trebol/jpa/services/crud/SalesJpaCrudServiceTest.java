package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SalesJpaCrudServiceTest {

  @Mock ISalesJpaRepository salesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellPojo, Sell> salesConverterMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;

  @Test
  public void sanity_check() {
    SalesJpaCrudServiceImpl service = new SalesJpaCrudServiceImpl(
        salesRepositoryMock,
        salesConverterMock,
        productsConverterMock
    );
    assertNotNull(service);
  }

}
