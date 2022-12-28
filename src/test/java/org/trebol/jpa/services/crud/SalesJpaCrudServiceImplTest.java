package org.trebol.jpa.services.crud;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.*;
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

  @BeforeEach
  void beforeEach() {
    resetSales();
  }

  @Test
  void finds_by_example_using_buy_order() {
    SellPojo input = sellPojoForFetch();
    Sell expectedResult = sellEntityAfterCreation();
    when(salesRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedResult));

    Optional<Sell> match = instance.getExisting(input);

    verify(salesRepositoryMock).findById(input.getBuyOrder());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void finds_using_predicates() throws EntityNotFoundException {
    SellPojo expectedResult = SellPojo.builder()
      .buyOrder(1L)
      .details(List.of()) // omit conversion cascading
      .build();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.readOne(new BooleanBuilder());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_sell()
      throws BadInputException, EntityExistsException {
    SellPojo input = sellPojoBeforeCreation();
    SellPojo expectedResult = sellPojoAfterCreation();
    when(salesConverterMock.convertToNewEntity(any(SellPojo.class))).thenReturn(sellEntityBeforeCreation());
    when(salesRepositoryMock.saveAndFlush(any(Sell.class))).thenReturn(sellEntityAfterCreation());
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.create(input);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void updates_sell()
      throws BadInputException, EntityNotFoundException {
    SellPojo input = sellPojoAfterCreation();
    Instant updatedDate = Instant.now().minus(Duration.ofHours(1L));
    input.setDate(updatedDate);
    Sell internalResult = new Sell(sellEntityAfterCreation());
    internalResult.setDate(updatedDate);
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(dataTransportServiceMock.applyChangesToExistingEntity(any(SellPojo.class), sellEntityAfterCreation())).thenReturn(internalResult);
    when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.of(productEntityAfterCreation()));
    when(salesRepositoryMock.saveAndFlush(any(Sell.class))).thenReturn(internalResult);
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(input);

    SellPojo result = instance.update(input, new BooleanBuilder());

    assertNotNull(result);
    assertEquals(input, result);
  }

  @Test
  void returns_same_when_no_update_is_made()
      throws BadInputException, EntityNotFoundException {
    SellPojo input = sellPojoAfterCreation();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(sellEntityAfterCreation()));
    when(productsRepository.findByBarcode(anyString())).thenReturn(Optional.of(productEntityAfterCreation()));
    when(dataTransportServiceMock.applyChangesToExistingEntity(any(SellPojo.class), any(Sell.class))).thenReturn(sellEntityAfterCreation());
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(input);

    SellPojo result = instance.update(input, new BooleanBuilder());

    verify(dataTransportServiceMock).applyChangesToExistingEntity(input, sellEntityAfterCreation());
    assertEquals(input, result);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> instance.readOne(new BooleanBuilder()));
  }
}
