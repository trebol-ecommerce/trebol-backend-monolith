package org.trebol.jpa.services.crud;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ProductsTestHelper.*;
import static org.trebol.testhelpers.SalesTestHelper.*;

@ExtendWith(MockitoExtension.class)
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
  public void finds_by_id_aka_buy_order()
      throws BadInputException {
    resetSales();
    when(salesRepositoryMock.findById(sellPojoForFetch().getBuyOrder())).thenReturn(Optional.of(sellEntityAfterCreation()));
    SalesJpaCrudServiceImpl service = instantiate();

    Optional<Sell> match = service.getExisting(sellPojoForFetch());

    assertTrue(match.isPresent());
    verify(salesRepositoryMock).findById(sellPojoForFetch().getBuyOrder());
    assertEquals(match.get().getId(), sellEntityAfterCreation().getId());
  }

  @Test
  public void finds_using_predicates()
      throws EntityNotFoundException {
    resetProducts();
    resetSales();
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(sellEntityAfterCreation())).thenReturn(sellPojoAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    SalesJpaCrudServiceImpl service = instantiate();

    SellPojo result = service.readOne(filters);

    assertNotNull(result);
    verify(salesRepositoryMock).findOne(filters);
    assertEquals(result.getBuyOrder(), sellPojoAfterCreation().getBuyOrder());
    assertEquals(result.getDate(), sellPojoAfterCreation().getDate());
  }

  @Test
  public void creates_sell()
      throws BadInputException, EntityExistsException {
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

  @Test
  public void updates_sell()
      throws BadInputException, EntityNotFoundException {
    resetSales();
    Instant updatedDate = Instant.now().minus(Duration.ofHours(1L));
    SellPojo sellPojoWithUpdates = new SellPojo(sellPojoAfterCreation());
    sellPojoWithUpdates.setDate(updatedDate);
    Sell sellEntityWithUpdates = new Sell(sellEntityAfterCreation());
    sellEntityWithUpdates.setDate(updatedDate);
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.applyChangesToExistingEntity(sellPojoWithUpdates, sellEntityAfterCreation())).thenReturn(sellEntityWithUpdates);
    when(salesRepositoryMock.saveAndFlush(sellEntityWithUpdates)).thenReturn(sellEntityWithUpdates);
    when(salesConverterMock.convertToPojo(sellEntityWithUpdates)).thenReturn(sellPojoWithUpdates);
    SalesJpaCrudServiceImpl service = this.instantiate();

    SellPojo result = service.update(sellPojoWithUpdates, filters);

    assertNotNull(result);
    verify(salesRepositoryMock).findOne(filters);
    verify(salesConverterMock).applyChangesToExistingEntity(sellPojoWithUpdates, sellEntityAfterCreation());
    verify(salesRepositoryMock).saveAndFlush(sellEntityWithUpdates);
    verify(salesConverterMock).convertToPojo(sellEntityWithUpdates);
    assertEquals(result.getBuyOrder(), sellPojoWithUpdates.getBuyOrder());
    assertEquals(result.getDate(), sellPojoWithUpdates.getDate());
  }

  @Test
  public void returns_same_when_no_update_is_made()
      throws BadInputException, EntityNotFoundException {
    resetSales();
    SellPojo copy = new SellPojo(sellPojoAfterCreation());
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.applyChangesToExistingEntity(copy, sellEntityAfterCreation())).thenReturn(sellEntityAfterCreation());
    SalesJpaCrudServiceImpl service = this.instantiate();

    SellPojo result = service.update(copy, filters);

    assertEquals(result, copy);
    verify(salesRepositoryMock).findOne(filters);
    verify(salesConverterMock).applyChangesToExistingEntity(copy, sellEntityAfterCreation());
  }

  @Test
  public void throws_exception_when_not_found_using_predicates() {
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.empty());
    SalesJpaCrudServiceImpl service = instantiate();

    SellPojo result = null;
    try {
      result = service.readOne(filters);
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
    }

    assertNull(result);
    verify(salesRepositoryMock).findOne(filters);
  }

  private SalesJpaCrudServiceImpl instantiate() {
    return new SalesJpaCrudServiceImpl(
        salesRepositoryMock,
        salesConverterMock,
        productsConverterMock
    );
  }

}
