package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellPojo;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    Long sellId = 1L;
    Instant date = Instant.now();
    int totalItems = 1;
    int netValue = 100;
    int transportValue = 0;
    int taxesValue = 0;
    int totalValue = 100;
    String transactionToken = "a";
    Customer customer = new Customer();
    PaymentType paymentType = new PaymentType();
    SellStatus status = new SellStatus();
    BillingType billingType = new BillingType();
    BillingCompany billingCompany = new BillingCompany();
    Address billingAddress = new Address();
    Shipper shipper = new Shipper();
    Address shippingAddress = new Address();
    Salesperson salesperson = new Salesperson();
    Collection<SellDetail> details = List.of(new SellDetail());
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

  private SalesJpaCrudServiceImpl instantiate() {
    return new SalesJpaCrudServiceImpl(
        salesRepositoryMock,
        salesConverterMock,
        productsConverterMock
    );
  }

}
