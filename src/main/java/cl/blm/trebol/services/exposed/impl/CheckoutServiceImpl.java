package cl.blm.trebol.services.exposed.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

import cl.blm.trebol.api.pojo.CustomerPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutResponsePojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutRequestPojo;
import cl.blm.trebol.api.pojo.WebpayValidationRequestPojo;
import cl.blm.trebol.api.pojo.WebpayValidationResponsePojo;
import cl.blm.trebol.config.CheckoutConfig;
import cl.blm.trebol.http.RestClient;
import cl.blm.trebol.jpa.entities.Customer;
import cl.blm.trebol.jpa.entities.Product;
import cl.blm.trebol.jpa.entities.QSell;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.jpa.entities.SellDetail;
import cl.blm.trebol.jpa.entities.SellStatus;
import cl.blm.trebol.jpa.entities.SellType;
import cl.blm.trebol.jpa.repositories.ProductsRepository;
import cl.blm.trebol.jpa.repositories.SalesRepository;
import cl.blm.trebol.services.exposed.CheckoutService;
import cl.blm.trebol.services.security.AuthenticatedPeopleService;
import cl.blm.trebol.jpa.repositories.CustomersRepository;
import cl.blm.trebol.services.user.CustomerPersonRelationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CheckoutServiceImpl
    implements CheckoutService {
  private final Logger LOG = LoggerFactory.getLogger(CheckoutServiceImpl.class);

  private final ConversionService conversionService;
  private final SalesRepository salesRepository;
  private final ProductsRepository productsRepository;
  private final CustomersRepository customersRepository;
  private final CheckoutConfig checkoutConfig;
  private final ObjectMapper objectMapper;
  private final AuthenticatedPeopleService authenticatedPeopleService;
  private final CustomerPersonRelationService customerPersonRelationService;

  @Autowired
  public CheckoutServiceImpl(
      ConversionService conversionService,
      SalesRepository salesRepository,
      ProductsRepository productsRepository,
      CustomersRepository customerRepository,
      CheckoutConfig checkoutConfig,
      ObjectMapper objectMapper,
      AuthenticatedPeopleService authenticatedPeopleService,
      CustomerPersonRelationService customerPersonRelationService) {
    this.conversionService = conversionService;
    this.salesRepository = salesRepository;
    this.productsRepository = productsRepository;
    this.customersRepository = customerRepository;
    this.checkoutConfig = checkoutConfig;
    this.objectMapper = objectMapper;
    this.authenticatedPeopleService = authenticatedPeopleService;
    this.customerPersonRelationService = customerPersonRelationService;
  }

  private int fetchCustomerId(String authorizationHeader) {
    PersonPojo authenticatedPerson = authenticatedPeopleService.fetchAuthenticatedUserPersonProfile(authorizationHeader);
    int personId = authenticatedPerson.getId();
    CustomerPojo authenticatedCustomer = customerPersonRelationService.getCustomerFromPersonId(personId);
    if (authenticatedCustomer != null) {
      int customerId = authenticatedCustomer.getId();
      return customerId;
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

  private String webpayTransactionAsJSON(WebpayCheckoutRequestPojo transaction) throws RuntimeException {
    String payload;
    try {
      payload = objectMapper.writeValueAsString(transaction);
    } catch (JsonProcessingException exc) {
      throw new RuntimeException("The transaction data could not be parsed as JSON");
    }
    return payload;
  }

  private String webpayValidationAsJSON(String token) throws RuntimeException {
    WebpayValidationRequestPojo source = new WebpayValidationRequestPojo(token);
    String payload;
    try {
      payload = objectMapper.writeValueAsString(source);
    } catch (JsonProcessingException exc) {
      throw new RuntimeException("The transaction data could not be parsed as JSON");
    }
    return payload;
  }

  private WebpayCheckoutResponsePojo requestTransactionToCheckoutServer(WebpayCheckoutRequestPojo transaction) throws RuntimeException {
    String payload = this.webpayTransactionAsJSON(transaction);
    String originUrl = checkoutConfig.getOriginURL();
    String serverUrl = checkoutConfig.getServerURL();
    String uri = checkoutConfig.getResourceURI();
    RestClient restClient = new RestClient(originUrl, serverUrl);
    LOG.info("Requesting a transaction to the checkout server. Transaction ID / Amount: {} / {}", transaction.getTransactionId(), transaction.getAmount());

    try {
      String requestResult = restClient.post(uri, payload);
      WebpayCheckoutResponsePojo data = objectMapper.readValue(requestResult, WebpayCheckoutResponsePojo.class);
      return data;
    } catch (RestClientException exc) {
      throw new RuntimeException("The checkout server had a problem starting the transaction", exc);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("The checkout server generated an incorrect response after starting the transaction", ex);
    }
  }

  private WebpayValidationResponsePojo requestValidationFromCheckoutServer(String transactionToken) throws RuntimeException {
    String payload = this.webpayValidationAsJSON(transactionToken);
    String originUrl = checkoutConfig.getOriginURL();
    String serverUrl = checkoutConfig.getServerURL();
    String uri = checkoutConfig.getTransactionValidationResourceURI();
    RestClient restClient = new RestClient(originUrl, serverUrl);

    try {
      String requestResult = restClient.post(uri, payload);
      LOG.debug("The validation request got the following raw response: {}", requestResult);
      WebpayValidationResponsePojo data = objectMapper.readValue(requestResult, WebpayValidationResponsePojo.class);
      return data;
    } catch (RestClientException exc) {
      throw new RuntimeException("The checkout server had a problem validating the transaction", exc);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("The checkout server generated an incorrect response after validating the transaction", ex);
    }
  }

  @Override
  public WebpayCheckoutRequestPojo saveCartAsTransactionRequest(String authorization, Collection<SellDetailPojo> cartDetails) {
    int customerId = this.fetchCustomerId(authorization);
    Customer customer = customersRepository.getOne(customerId);
    int totalValue = calculateTotalCartValue(cartDetails);
    List<SellDetail> entityDetails = new ArrayList<>();
    for (SellDetailPojo p : cartDetails) {
      SellDetail e = conversionService.convert(p, SellDetail.class);
      entityDetails.add(e);
    }

    String session = authorization.substring(authorization.length()-21,authorization.length()-1);

    Date date = Date.from(Instant.now());
    SellType sellType = new SellType();
    sellType.setId(1);

    Sell target = new Sell();
    target.setDate(date);
    target.setType(sellType);
    target.setCustomer(customer);
    target.setStatus(new SellStatus(1));
    target.setSessionExtract(session);
    target.setTotalValue(totalValue);
    target.setTotalItems(cartDetails.size());
    target.setDetails(entityDetails);
    target = salesRepository.saveAndFlush(target);

    SellPojo result = conversionService.convert(target, SellPojo.class);

    WebpayCheckoutRequestPojo transaction = new WebpayCheckoutRequestPojo();
    transaction.setTransactionId(result.getId().toString());
    transaction.setSessionId(session);
    transaction.setAmount(totalValue);
    return transaction;
  }

  @Override
  public WebpayCheckoutResponsePojo startWebpayTransaction(WebpayCheckoutRequestPojo transaction) {
    WebpayCheckoutResponsePojo response = this.requestTransactionToCheckoutServer(transaction);

    Sell current = salesRepository.getOne(Integer.valueOf(transaction.getTransactionId()));
    current.setStatus(new SellStatus(2));
    current.setToken(response.getToken());
    salesRepository.saveAndFlush(current);

    return response;
  }

  @Override
  public Integer confirmWebpayTransactionResult(String transactionToken) {
    QSell qSell = QSell.sell;
    Predicate associatedToTransactionToken = qSell.token.eq(transactionToken);
    Optional<Sell> match = salesRepository.findOne(associatedToTransactionToken);
    if (!match.isPresent()) {
      throw new RuntimeException("The provided token did not match any sell transaction");
    }
    Sell foundMatch = match.get();

    WebpayValidationResponsePojo validationResponse = this.requestValidationFromCheckoutServer(transactionToken);
    Integer responseCode = validationResponse.getResponseCode();
    LOG.debug("The response code of this transaction was: {}", responseCode);

    switch (responseCode) {
      case 0:
        foundMatch.setStatus(new SellStatus(4));
        salesRepository.saveAndFlush(foundMatch);
        break;
    }

    Integer buyOrder = Integer.valueOf(validationResponse.getBuyOrder());

    return buyOrder;
  }

}
