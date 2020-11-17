package cl.blm.trebol.services.exposed.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.config.CheckoutConfig;
import cl.blm.trebol.http.RestClient;
import cl.blm.trebol.jpa.entities.Client;
import cl.blm.trebol.jpa.entities.Product;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.jpa.entities.SellDetail;
import cl.blm.trebol.jpa.entities.SellType;
import cl.blm.trebol.jpa.repositories.ClientsRepository;
import cl.blm.trebol.jpa.repositories.ProductsRepository;
import cl.blm.trebol.jpa.repositories.SalesRepository;
import cl.blm.trebol.services.exposed.CheckoutService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CheckoutServiceImpl
    implements CheckoutService {

  private final ConversionService conversionService;
  private final SalesRepository salesRepository;
  private final ProductsRepository productsRepository;
  private final ClientsRepository clientsRepository;
  private final CheckoutConfig checkoutConfig;

  @Autowired
  public CheckoutServiceImpl(ConversionService conversionService, SalesRepository salesRepository,
      ProductsRepository productsRepository, ClientsRepository clientsRepository, CheckoutConfig checkoutConfig) {
    this.conversionService = conversionService;
    this.salesRepository = salesRepository;
    this.productsRepository = productsRepository;
    this.clientsRepository = clientsRepository;
    this.checkoutConfig = checkoutConfig;
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
  public SellPojo saveCartAsTransactionRequest(Integer clientId, Collection<SellDetailPojo> cartDetails) {

    Client client = clientsRepository.getOne(clientId);
    int totalValue = calculateTotalCartValue(cartDetails);
    List<SellDetail> entityDetails = new ArrayList<>();
    for (SellDetailPojo p : cartDetails) {
      SellDetail e = conversionService.convert(p, SellDetail.class);
      entityDetails.add(e);
    }

    Date date = Date.from(Instant.now());
    SellType sellType = new SellType();
    sellType.setId(1);

    Sell target = new Sell();
    target.setDate(date);
    target.setSellType(sellType);
    target.setSellDetails(entityDetails);
    target.setSubtotal(totalValue);
    target.setClient(client);
    target = salesRepository.saveAndFlush(target);

    SellPojo result = conversionService.convert(target, SellPojo.class);
    return result;
  }

  @Override
  public WebPayRedirectionData startWebpayTransaction(SellPojo sellTransaction) {
    ObjectMapper jsonMapper = new JsonMapper();
    String payload;
    try {
      payload = jsonMapper.writeValueAsString(sellTransaction);
    } catch (JsonProcessingException exc) {
      throw new RuntimeException("The transaction data could not be parsed as JSON");
    }

    String serverURL = checkoutConfig.getServerURL();
    RestClient restClient = new RestClient(serverURL);
    String requestResult = restClient.post("", payload);
    if (restClient.getStatus().equals(HttpStatus.OK)) {
      WebPayRedirectionData data = jsonMapper.convertValue(requestResult, WebPayRedirectionData.class);
      return data;
    }
    throw new RuntimeException("The transaction could not be started");
  }

  @Override
  public Integer confirmWebpayTransactionResult(String transactionToken) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
