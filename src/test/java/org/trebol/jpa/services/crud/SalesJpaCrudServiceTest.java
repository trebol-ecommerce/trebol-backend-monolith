package org.trebol.jpa.services.crud;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
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
class SalesJpaCrudServiceTest {
  @Mock ISalesJpaRepository salesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellPojo, Sell> salesConverterMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;
  private GenericCrudJpaService<SellPojo, Sell> instance;

  @BeforeEach
  void setUp() {
    instance = new SalesJpaCrudServiceImpl(
            salesRepositoryMock,
            salesConverterMock,
            productsConverterMock
    );
  }

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }

  @Test
  void finds_by_id_aka_buy_order()
      throws BadInputException {
    resetSales();
    when(salesRepositoryMock.findById(sellPojoForFetch().getBuyOrder())).thenReturn(Optional.of(sellEntityAfterCreation()));

    Optional<Sell> match = instance.getExisting(sellPojoForFetch());

    verify(salesRepositoryMock).findById(sellPojoForFetch().getBuyOrder());
    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), sellEntityAfterCreation().getId());
  }

  @Test
  void finds_using_predicates()
      throws EntityNotFoundException {
    resetProducts();
    resetSales();
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(sellEntityAfterCreation())).thenReturn(sellPojoAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());

    SellPojo result = instance.readOne(filters);

    verify(salesRepositoryMock).findOne(filters);
    assertNotNull(result);
    assertEquals(result.getBuyOrder(), sellPojoAfterCreation().getBuyOrder());
    assertEquals(result.getDate(), sellPojoAfterCreation().getDate());
  }

  @Test
  void creates_sell()
      throws BadInputException, EntityExistsException {
    resetSales();
    when(salesConverterMock.convertToNewEntity(sellPojoBeforeCreation())).thenReturn(sellEntityBeforeCreation());
    when(salesRepositoryMock.saveAndFlush(sellEntityBeforeCreation())).thenReturn(sellEntityAfterCreation());
    when(salesConverterMock.convertToPojo(sellEntityAfterCreation())).thenReturn(sellPojoAfterCreation());

    SellPojo result = instance.create(sellPojoBeforeCreation());

    verify(salesConverterMock).convertToNewEntity(sellPojoBeforeCreation());
    verify(salesRepositoryMock).saveAndFlush(sellEntityBeforeCreation());
    verify(salesConverterMock).convertToPojo(sellEntityAfterCreation());
    assertNotNull(result);
    assertEquals(result.getBuyOrder(), sellPojoAfterCreation().getBuyOrder());
    assertEquals(result.getDate(), sellPojoAfterCreation().getDate());
    assertEquals(result.getPaymentType(), sellPojoAfterCreation().getPaymentType());
    assertEquals(result.getStatus(), sellPojoAfterCreation().getStatus());
    assertEquals(result.getTotalValue(), sellPojoAfterCreation().getTotalValue());
    assertEquals(result.getTotalItems(), sellPojoAfterCreation().getTotalItems());
    assertEquals(result.getCustomer().getId(), sellPojoAfterCreation().getCustomer().getId());
  }

  @Test
  void updates_sell()
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

    SellPojo result = instance.update(sellPojoWithUpdates, filters);

    verify(salesRepositoryMock).findOne(filters);
    verify(salesConverterMock).applyChangesToExistingEntity(sellPojoWithUpdates, sellEntityAfterCreation());
    verify(salesRepositoryMock).saveAndFlush(sellEntityWithUpdates);
    verify(salesConverterMock).convertToPojo(sellEntityWithUpdates);
    assertNotNull(result);
    assertEquals(result.getBuyOrder(), sellPojoWithUpdates.getBuyOrder());
    assertEquals(result.getDate(), sellPojoWithUpdates.getDate());
  }

  @Test
  void returns_same_when_no_update_is_made()
      throws BadInputException, EntityNotFoundException {
    resetSales();
    SellPojo copy = new SellPojo(sellPojoAfterCreation());
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.applyChangesToExistingEntity(copy, sellEntityAfterCreation())).thenReturn(sellEntityAfterCreation());

    SellPojo result = instance.update(copy, filters);

    verify(salesRepositoryMock).findOne(filters);
    verify(salesConverterMock).applyChangesToExistingEntity(copy, sellEntityAfterCreation());
    assertEquals(result, copy);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> instance.readOne(filters));
  }
}
