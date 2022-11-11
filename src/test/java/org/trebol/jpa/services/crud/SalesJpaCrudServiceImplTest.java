package org.trebol.jpa.services.crud;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ProductsTestHelper.productEntityAfterCreation;
import static org.trebol.testhelpers.SalesTestHelper.*;

@ExtendWith(MockitoExtension.class)
class SalesJpaCrudServiceImplTest {
  @InjectMocks SalesJpaCrudServiceImpl instance;
  @Mock ISalesJpaRepository salesRepositoryMock;
  @Mock IProductsJpaRepository productsRepository;
  @Mock ITwoWayConverterJpaService<SellPojo, Sell> salesConverterMock;
  @Mock IDataTransportJpaService<SellPojo, Sell> dataTransportServiceMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock; // TODO write an unit test that needs this mock

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
  void finds_using_predicates() throws EntityNotFoundException {
    final SellPojo targetFoundSellPojo = SellPojo.builder()
      .buyOrder(1L)
      .details(List.of()) // omit conversion cascading
      .build();
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(targetFoundSellPojo);

    SellPojo result = instance.readOne(filters);

    assertNotNull(result);
    assertEquals(result.getBuyOrder(), targetFoundSellPojo.getBuyOrder());
  }

  @Test
  void creates_sell()
      throws BadInputException, EntityExistsException {
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
    Instant updatedDate = Instant.now().minus(Duration.ofHours(1L));
    SellPojo sellPojoWithUpdates = sellPojoAfterCreation();
    sellPojoWithUpdates.setDate(updatedDate);
    Sell sellEntityWithUpdates = new Sell(sellEntityAfterCreation());
    sellEntityWithUpdates.setDate(updatedDate);
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(dataTransportServiceMock.applyChangesToExistingEntity(sellPojoWithUpdates, sellEntityAfterCreation())).thenReturn(sellEntityWithUpdates);
    when(productsRepository.findByBarcode(any())).thenReturn(Optional.of(productEntityAfterCreation()));
    when(salesRepositoryMock.saveAndFlush(sellEntityWithUpdates)).thenReturn(sellEntityWithUpdates);
    when(salesConverterMock.convertToPojo(sellEntityWithUpdates)).thenReturn(sellPojoWithUpdates);

    SellPojo result = instance.update(sellPojoWithUpdates, filters);

    verify(salesRepositoryMock).findOne(filters);
    verify(dataTransportServiceMock).applyChangesToExistingEntity(sellPojoWithUpdates, sellEntityAfterCreation());
    verify(salesRepositoryMock).saveAndFlush(sellEntityWithUpdates);
    verify(salesConverterMock).convertToPojo(sellEntityWithUpdates);
    assertNotNull(result);
    assertEquals(result.getBuyOrder(), sellPojoWithUpdates.getBuyOrder());
    assertEquals(result.getDate(), sellPojoWithUpdates.getDate());
  }

  @Test
  void returns_same_when_no_update_is_made()
      throws BadInputException, EntityNotFoundException {
    SellPojo copy = sellPojoAfterCreation();
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(productsRepository.findByBarcode(any())).thenReturn(Optional.of(productEntityAfterCreation()));
    when(dataTransportServiceMock.applyChangesToExistingEntity(copy, sellEntityAfterCreation())).thenReturn(sellEntityAfterCreation());

    SellPojo result = instance.update(copy, filters);

    verify(salesRepositoryMock).findOne(filters);
    verify(dataTransportServiceMock).applyChangesToExistingEntity(copy, sellEntityAfterCreation());
    assertEquals(result, copy);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    Predicate filters = new BooleanBuilder();
    when(salesRepositoryMock.findOne(filters)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> instance.readOne(filters));
  }
}
