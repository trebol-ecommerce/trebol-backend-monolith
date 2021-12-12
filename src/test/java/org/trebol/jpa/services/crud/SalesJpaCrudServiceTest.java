package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.jpa.testhelpers.SalesJpaCrudServiceTestHelper.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesJpaCrudServiceTest {

  @Mock ISalesJpaRepository salesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellPojo, Sell> salesConverterMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;

  @Test
  public void sanity_check() {
    SalesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_id_aka_buy_order() throws BadInputException {
    resetSales();
    when(salesRepositoryMock.findById(sellPojoForFetch().getBuyOrder())).thenReturn(Optional.of(sellEntityAfterCreation()));
    SalesJpaCrudServiceImpl service = instantiate();

    Optional<Sell> match = service.getExisting(sellPojoForFetch());

    assertTrue(match.isPresent());
    verify(salesRepositoryMock).findById(sellPojoForFetch().getBuyOrder());
    assertEquals(match.get().getId(), sellEntityAfterCreation().getId());
  }

  @Test
  public void creates_sell() throws BadInputException, EntityAlreadyExistsException {
    resetSales();
    when(salesConverterMock.convertToNewEntity(sellPojoBeforeCreation())).thenReturn(sellEntityBeforeCreation());
    when(salesRepositoryMock.saveAndFlush(sellEntityBeforeCreation())).thenReturn(sellEntityAfterCreation());
    when(salesConverterMock.convertToPojo(sellEntityAfterCreation())).thenReturn(sellPojoAfterCreation());
    SalesJpaCrudServiceImpl service = this.instantiate();

    SellPojo result = service.create(sellPojoBeforeCreation());

    assertNotNull(result);
    verify(salesConverterMock).convertToNewEntity(sellPojoBeforeCreation());
    verify(salesRepositoryMock).saveAndFlush(sellEntityBeforeCreation());
    verify(salesConverterMock).convertToPojo(sellEntityAfterCreation());
    assertEquals(result.getBuyOrder(), sellPojoAfterCreation().getBuyOrder());
    assertEquals(result.getDate(), sellPojoAfterCreation().getDate());
    assertEquals(result.getPaymentType(), sellPojoAfterCreation().getPaymentType());
    assertEquals(result.getStatus(), sellPojoAfterCreation().getStatus());
    assertEquals(result.getTotalValue(), sellPojoAfterCreation().getTotalValue());
    assertEquals(result.getTotalItems(), sellPojoAfterCreation().getTotalItems());
    assertEquals(result.getCustomer().getId(), sellPojoAfterCreation().getCustomer().getId());
  }

  private SalesJpaCrudServiceImpl instantiate() {
    return new SalesJpaCrudServiceImpl(
        salesRepositoryMock,
        salesConverterMock,
        productsConverterMock
    );
  }

}
