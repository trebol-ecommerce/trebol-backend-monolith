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

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.api.pojo.WebpayTransactionPojo;
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
import cl.blm.trebol.services.security.AuthenticatedPeopleService;
import cl.blm.trebol.services.user.ClientPersonRelationService;

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
  private final ObjectMapper objectMapper;
  private final AuthenticatedPeopleService authenticatedPeopleService;
  private final ClientPersonRelationService clientPersonRelationService;

  @Autowired
  public CheckoutServiceImpl(ConversionService conversionService, SalesRepository salesRepository,
      ProductsRepository productsRepository, ClientsRepository clientsRepository, CheckoutConfig checkoutConfig,
      ObjectMapper objectMapper, AuthenticatedPeopleService authenticatedPeopleService,
      ClientPersonRelationService clientPersonRelationService) {
    this.conversionService = conversionService;
    this.salesRepository = salesRepository;
    this.productsRepository = productsRepository;
    this.clientsRepository = clientsRepository;
    this.checkoutConfig = checkoutConfig;
    this.objectMapper = objectMapper;
    this.authenticatedPeopleService = authenticatedPeopleService;
    this.clientPersonRelationService = clientPersonRelationService;
  }

  private int fetchClientId(String authorizationHeader) {
    PersonPojo authenticatedPerson = authenticatedPeopleService.fetchAuthenticatedUserPersonProfile(authorizationHeader);
    int personId = authenticatedPerson.getId();
    ClientPojo authenticatedClient = clientPersonRelationService.getClientFromPersonId(personId);
    if (authenticatedClient != null) {
      int clientId = authenticatedClient.getId();
      return clientId;
    }
    throw new RuntimeException("The user requesting a cart checkout does not have an associated client ID");
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

  private String webpayTransactionAsJSON(WebpayTransactionPojo transaction) throws RuntimeException {
    String payload;
    try {
      payload = objectMapper.writeValueAsString(transaction);
    } catch (JsonProcessingException exc) {
      throw new RuntimeException("The transaction data could not be parsed as JSON");
    }
    return payload;
  }

  @Override
  public WebpayTransactionPojo saveCartAsTransactionRequest(String authorization, Collection<SellDetailPojo> cartDetails) {
    int clientId = this.fetchClientId(authorization);
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
    WebpayTransactionPojo transaction = new WebpayTransactionPojo();
    transaction.setTr_id(result.getId().toString());
    transaction.setTr_session(authorization);
    transaction.setTr_amount(totalValue);
    return transaction;
  }

  @Override
  public WebPayRedirectionData startWebpayTransaction(WebpayTransactionPojo transaction) {
    String payload = this.webpayTransactionAsJSON(transaction);
    String originUrl = checkoutConfig.getOriginURL();
    String serverUrl = checkoutConfig.getServerURL();
    String uri = checkoutConfig.getResourceURI();
    RestClient restClient = new RestClient(originUrl, serverUrl);
    String requestResult = restClient.post(uri, payload);
    if (restClient.getStatus().equals(HttpStatus.OK)) {
      WebPayRedirectionData data = objectMapper.convertValue(requestResult, WebPayRedirectionData.class);
      return data;
    }
    throw new RuntimeException("The transaction could not be started");
  }

  @Override
  public Integer confirmWebpayTransactionResult(String transactionToken) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
