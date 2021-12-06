package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalesJpaCrudServiceTest {

  @Mock ISalesJpaRepository salesRepositoryMock;
  @Mock ITwoWayConverterJpaService<SellPojo, Sell> salesConverterMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;

  private final Long sellId = 1L;
  private final int totalItems = 1;
  private final int netValue = 100;
  private final int transportValue = 0;
  private final int taxesValue = 0;
  private final int totalValue = 100;
  private final String transactionToken = "a";

  @Test
  public void sanity_check() {
    SalesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_id_aka_buy_order() throws BadInputException {
    Instant date = Instant.now();
    Customer customer = new Customer();
    PaymentType paymentType = new PaymentType();
    SellStatus status = new SellStatus();
    BillingType billingType = new BillingType();
    BillingCompany billingCompany = new BillingCompany();
    Address billingAddress = new Address();
    Shipper shipper = new Shipper();
    Address shippingAddress = new Address();
    Salesperson salesperson = new Salesperson();
    SellDetail sellDetail = new SellDetail();
    Collection<SellDetail> details = List.of(sellDetail);
    SellPojo example = new SellPojo(sellId);
    Sell persistedEntity = new Sell(sellId, date, totalItems, netValue, transportValue, taxesValue, totalValue,
      transactionToken, customer, paymentType, status, billingType, billingCompany, billingAddress, shipper,
      shippingAddress, salesperson, details);
    when(salesRepositoryMock.findById(sellId)).thenReturn(Optional.of(persistedEntity));
    SalesJpaCrudServiceImpl service = instantiate();

    Optional<Sell> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), sellId);
    assertEquals(match.get().getDate(), date);
    assertEquals(match.get().getTotalItems(), totalItems);
    assertEquals(match.get().getNetValue(), netValue);
    assertEquals(match.get().getTransportValue(), transportValue);
    assertEquals(match.get().getTaxesValue(), taxesValue);
    assertEquals(match.get().getTotalValue(), totalValue);
    assertEquals(match.get().getTransactionToken(), transactionToken);
    assertEquals(match.get().getCustomer(), customer);
    assertEquals(match.get().getPaymentType(), paymentType);
    assertEquals(match.get().getStatus(), status);
    assertEquals(match.get().getBillingType(), billingType);
    assertEquals(match.get().getBillingCompany(), billingCompany);
    assertEquals(match.get().getBillingAddress(), billingAddress);
    assertEquals(match.get().getShipper(), shipper);
    assertEquals(match.get().getShippingAddress(), shippingAddress);
    assertEquals(match.get().getSalesperson(), salesperson);
    assertEquals(match.get().getDetails(), details);
  }

  @Test
  public void creates_sell() throws BadInputException, EntityAlreadyExistsException {
    Instant date = Instant.now();
    long productId = 1L;
    String productName = "test product name";
    String productBarcode = "TESTPROD1";
    String productDescription = "test product description";
    int productPrice = 100;
    int productStock = 10;
    int productStockCritical = 1;
    long sellDetailId = 1;
    int sellDetailUnits = 1;
    long sellId = 1L;
    long sellBillingTypeId = 1L;
    String sellBillingTypeName = "Bill";
    long sellPaymentTypeId = 1L;
    String sellPaymentTypeName = "WebPay Plus";
    String customerIdNumber = "111111111";
    long sellStatusId = 1L;
    int sellStatusCode = 0;
    String sellStatusName = "Requested";
    ProductPojo productPojo = new ProductPojo(productBarcode);
    SellDetailPojo newDetailPojo = new SellDetailPojo(sellDetailUnits, productPojo);
    CustomerPojo customerPojo = new CustomerPojo(customerIdNumber);
    SellPojo newSellPojo = new SellPojo(List.of(newDetailPojo), sellBillingTypeName, sellPaymentTypeName, customerPojo);
    Product productEntity = new Product(productBarcode);
    Customer customerEntity = new Customer(customerIdNumber);
    PaymentType paymentTypeEntity = new PaymentType(sellPaymentTypeId, sellPaymentTypeName);
    BillingType billingTypeEntity = new BillingType(sellBillingTypeId, sellBillingTypeName);
    SellDetail newDetailEntity = new SellDetail(sellDetailUnits, productEntity);
    Sell newSellEntity = new Sell(customerEntity, paymentTypeEntity, billingTypeEntity, List.of(newDetailEntity));
    Product persistedProductEntity = new Product(productId, productName, productBarcode, productDescription,
        productPrice, productStock, productStockCritical, null);
    SellDetail persistedDetailEntity = new SellDetail(sellDetailId, sellDetailUnits, productPrice,
        persistedProductEntity);
    SellStatus sellStatusEntity = new SellStatus(sellStatusId, sellStatusCode, sellStatusName);
    Sell persistedSellEntity = new Sell(sellId, date, totalItems, netValue, transportValue, taxesValue, totalValue,
        transactionToken, customerEntity, paymentTypeEntity, sellStatusEntity, billingTypeEntity, null, null, null,
        null, null, List.of(persistedDetailEntity));
    ProductPojo persistedProductPojo = new ProductPojo(productId, productName, productBarcode, productPrice, null,
        productDescription, productStock, productStockCritical, List.of());
    SellDetailPojo persistedDetailPojo = new SellDetailPojo(sellDetailId, sellDetailUnits, productPrice,
        persistedProductPojo);
    CustomerPojo persistedCustomerPojo = new CustomerPojo(customerIdNumber);
    SellPojo persistedSellPojo = new SellPojo(sellId, transactionToken, date, List.of(persistedDetailPojo), netValue,
        taxesValue, transportValue, totalValue, totalItems, sellStatusName, sellBillingTypeName, sellPaymentTypeName,
        persistedCustomerPojo, null, null, null, null, null);
    when(salesConverterMock.convertToNewEntity(newSellPojo)).thenReturn(newSellEntity);
    when(salesRepositoryMock.saveAndFlush(newSellEntity)).thenReturn(persistedSellEntity);
    when(salesConverterMock.convertToPojo(persistedSellEntity)).thenReturn(persistedSellPojo);
    SalesJpaCrudServiceImpl service = this.instantiate();

    SellPojo result = service.create(newSellPojo);

    assertNotNull(result);
    assertEquals(result.getBuyOrder(), sellId);
    verify(salesConverterMock).convertToNewEntity(newSellPojo);
    verify(salesRepositoryMock).saveAndFlush(newSellEntity);
    verify(salesConverterMock).convertToPojo(persistedSellEntity);
  }

  private SalesJpaCrudServiceImpl instantiate() {
    return new SalesJpaCrudServiceImpl(
        salesRepositoryMock,
        salesConverterMock,
        productsConverterMock
    );
  }

}
