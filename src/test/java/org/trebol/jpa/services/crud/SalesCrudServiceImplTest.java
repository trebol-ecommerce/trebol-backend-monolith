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
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.AddressesJpaRepository;
import org.trebol.jpa.repositories.BillingTypesJpaRepository;
import org.trebol.jpa.repositories.ProductsJpaRepository;
import org.trebol.jpa.repositories.SalesJpaRepository;
import org.trebol.jpa.services.conversion.*;
import org.trebol.jpa.services.datatransport.SalesDataTransportService;
import org.trebol.pojo.AddressPojo;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;
import org.trebol.testhelpers.CustomersTestHelper;
import org.trebol.testhelpers.ProductsTestHelper;
import org.trebol.testhelpers.SalesTestHelper;

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
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class SalesCrudServiceImplTest {
  @InjectMocks SalesCrudServiceImpl instance;
  @Mock SalesJpaRepository salesRepositoryMock;
  @Mock ProductsJpaRepository productsRepositoryMock;
  @Mock SalesConverterService salesConverterMock;
  @Mock SalesDataTransportService salesDataTransportServiceMock;
  @Mock ProductsConverterService productsConverterServiceMock; // TODO write an unit test that needs this mock
  @Mock CustomersCrudService customersCrudServiceMock;
  @Mock CustomersConverterService customersConverterServiceMock;
  @Mock BillingTypesJpaRepository billingTypesRepositoryMock;
  @Mock BillingCompaniesCrudService billingCompaniesCrudServiceMock;
  @Mock BillingCompaniesConverterService billingCompaniesConverterServiceMock;
  @Mock AddressesJpaRepository addressesRepositoryMock;
  @Mock ShippersCrudService shippersCrudServiceMock;
  @Mock AddressesConverterService addressesConverterServiceMock;
  ProductsTestHelper productsHelper = new ProductsTestHelper();
  SalesTestHelper salesHelper = new SalesTestHelper();
  CustomersTestHelper customersHelper = new CustomersTestHelper();

  @BeforeEach
  void beforeEach() {
    productsHelper.resetProducts();
    salesHelper.resetSales();
  }

  @Test
  void finds_by_example_using_buy_order() {
    SellPojo input = salesHelper.sellPojoForFetch();
    Sell expectedResult = salesHelper.sellEntityAfterCreation();
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
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(salesHelper.sellEntityAfterCreation()));
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.readOne(new BooleanBuilder());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void does_not_create_sales_with_incomplete_data() {
    List.of(
      SellPojo.builder().build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .billingType(ANY)
        .build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .billingType(ANY)
        .billingAddress(AddressPojo.builder().build())
        .build(),
      SellPojo.builder()
        .customer(customersHelper.customerPojoBeforeCreation())
        .billingType(ANY)
        .billingAddress(AddressPojo.builder().build())
        .details(List.of())
        .build(),
      salesHelper.sellPojoBeforeCreation()
    ).forEach((sellPojo) -> {
      assertThrows(BadInputException.class, () -> instance.create(sellPojo));
    });
  }

  @Test
  void creates_sell()
    throws BadInputException, EntityExistsException {
    SellPojo input = SellPojo.builder()
      .customer(customersHelper.customerPojoBeforeCreation())
      .billingType(ANY)
      .billingAddress(AddressPojo.builder().build())
      .details(List.of(
        SellDetailPojo.builder()
          .units(1)
          .product(productsHelper.productPojoBeforeCreation())
          .build()
      ))
      .build();
    SellPojo expectedResult = salesHelper.sellPojoAfterCreation();
    Customer customerEntityInput = customersHelper.customerEntityBeforeCreation();
    Sell sellEntityInput = salesHelper.sellEntityBeforeCreation();
    Product productEntityInput = productsHelper.productEntityAfterCreation();
    Sell sellEntityOutput = salesHelper.sellEntityAfterCreation();
    when(salesConverterMock.convertToNewEntity(any(SellPojo.class))).thenReturn(sellEntityInput);
    when(customersCrudServiceMock.getExisting(any(CustomerPojo.class))).thenReturn(Optional.empty());
    when(customersConverterServiceMock.convertToNewEntity(any(CustomerPojo.class))).thenReturn(customerEntityInput);
    when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(new BillingType()));
    when(addressesConverterServiceMock.convertToNewEntity(any(AddressPojo.class))).thenReturn(new Address());
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(productEntityInput));
    when(salesRepositoryMock.saveAndFlush(any(Sell.class))).thenReturn(sellEntityOutput);
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(expectedResult);

    SellPojo result = instance.create(input);

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void updates_sell()
    throws BadInputException, EntityNotFoundException {
    SellPojo input = salesHelper.sellPojoAfterCreation();
    Instant updatedDate = Instant.now().minus(Duration.ofHours(1L));
    input.setDate(updatedDate);
    Sell internalResult = new Sell(salesHelper.sellEntityAfterCreation());
    internalResult.setDate(updatedDate);
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(salesHelper.sellEntityAfterCreation()));
    when(salesDataTransportServiceMock.applyChangesToExistingEntity(any(SellPojo.class), any(Sell.class))).thenReturn(internalResult);
    when(salesRepositoryMock.saveAndFlush(any(Sell.class))).thenReturn(internalResult);
    when(salesConverterMock.convertToPojo(any(Sell.class))).thenReturn(input);

    SellPojo result = instance.update(input, new BooleanBuilder());

    assertNotNull(result);
    assertEquals(input, result);
  }

  @Test
  void returns_same_when_no_update_is_made()
    throws BadInputException, EntityNotFoundException {
    SellPojo input = salesHelper.sellPojoAfterCreation();
    Sell matchingEntity = salesHelper.sellEntityAfterCreation();
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(matchingEntity));
    when(salesDataTransportServiceMock.applyChangesToExistingEntity(any(SellPojo.class), any(Sell.class))).thenReturn(matchingEntity);

    SellPojo result = instance.update(input, new BooleanBuilder());

    verify(salesDataTransportServiceMock).applyChangesToExistingEntity(input, matchingEntity);
    assertEquals(input, result);
  }

  @Test
  void throws_exception_when_not_found_using_predicates() {
    when(salesRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> instance.readOne(new BooleanBuilder()));
  }
}
