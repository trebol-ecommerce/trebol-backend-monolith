package cl.blm.trebol.services.exposed.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.jpa.entities.Product;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.jpa.entities.SellDetail;
import cl.blm.trebol.jpa.repositories.ProductsRepository;
import cl.blm.trebol.jpa.repositories.SalesRepository;
import cl.blm.trebol.services.exposed.CheckoutService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class CheckoutServiceImpl
    implements CheckoutService {

  private final ConversionService conversionService;
  private final SalesRepository salesRepository;
  private final ProductsRepository productsRepository;

  @Autowired
  public CheckoutServiceImpl(ConversionService conversionService, SalesRepository salesRepository,
      ProductsRepository productsRepository) {
    this.conversionService = conversionService;
    this.salesRepository = salesRepository;
    this.productsRepository = productsRepository;
  }

  private int calculateTotalCartValue(Collection<SellDetailPojo> cartDetails) {
    int value = 0;
    for (SellDetailPojo p : cartDetails) {
      Integer productId = p.getProduct().getId();
      Product product = productsRepository.getOne(productId);
      value += product.getPrice();
    }
    return value;
  }

  @Override
  public SellPojo saveCartAsTransactionRequest(Collection<SellDetailPojo> cartDetails) {
    int totalValue = calculateTotalCartValue(cartDetails);
    List<SellDetail> entityDetails = new ArrayList<>();
    for (SellDetailPojo p : cartDetails) {
      SellDetail e = conversionService.convert(p, SellDetail.class);
      entityDetails.add(e);
    }

    Sell target = new Sell();
    target.setSellDetails(entityDetails);
    target.setSubtotal(totalValue);
    target.setClient(null); // TODO set client to the API consumer, identified by its USER TOKEN
    target = salesRepository.saveAndFlush(target);

    SellPojo result = conversionService.convert(target, SellPojo.class);
    return result;
  }

  @Override
  public WebPayRedirectionData startWebpayTransaction(SellPojo sellTransaction) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Integer confirmWebpayTransactionResult(String transactionToken) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
