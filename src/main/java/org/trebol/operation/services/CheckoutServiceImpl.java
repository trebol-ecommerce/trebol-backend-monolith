package org.trebol.operation.services;

import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.integration.IPaymentsIntegrationService;
import org.trebol.integration.exceptions.PaymentServiceException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISellStepperJpaService;
import org.trebol.operation.ICheckoutService;
import org.trebol.pojo.PaymentRedirectionDetailsPojo;
import org.trebol.pojo.SellPojo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@Service
public class CheckoutServiceImpl
    implements ICheckoutService {

  private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
  private final GenericCrudJpaService<SellPojo, Sell> salesCrudService;
  private final ISellStepperJpaService sellStepperService;
  private final IPredicateJpaService<Sell> salesPredicateService;
  private final IPaymentsIntegrationService paymentIntegrationService;

  @Autowired
  public CheckoutServiceImpl(GenericCrudJpaService<SellPojo, Sell> salesCrudService,
                             ISellStepperJpaService sellStepperService,
                             IPredicateJpaService<Sell> salesPredicateService,
                             IPaymentsIntegrationService paymentIntegrationService) {
    this.salesCrudService = salesCrudService;
    this.sellStepperService = sellStepperService;
    this.salesPredicateService = salesPredicateService;
    this.paymentIntegrationService = paymentIntegrationService;
  }

  @Override
  public SellPojo saveCartAsPendingTransaction(SellPojo transaction) throws BadInputException {
    /*String hash = String.valueOf(transaction.hashCode());
    transaction.setStatus("Pending");
    if (transaction.getType() == null) {
      transaction.setType("Bill");
    }*/
    try {
      return salesCrudService.create(transaction);
    } catch (EntityAlreadyExistsException exc) {
      logger.error("Could not create a new sell", exc);
      throw new RuntimeException("The server had a problem requesting the transaction");
    }
  }

  @Override
  public PaymentRedirectionDetailsPojo requestTransactionStart(SellPojo transaction) throws PaymentServiceException {
    PaymentRedirectionDetailsPojo response = paymentIntegrationService.requestNewPaymentPageDetails(transaction);
    try {
      sellStepperService.setSellStatusToPaymentStartedWithToken(transaction.getBuyOrder(), response.getToken());
      return response;
    } catch (NotFoundException exc) {
      logger.error("A sell that was just created could not be found", exc);
      throw new RuntimeException("The server had a problem requesting the transaction");
    }
  }

  @Override
  public URI confirmTransaction(String transactionToken, boolean wasAborted) throws NotFoundException, PaymentServiceException {
    try {
      if (wasAborted) {
        SellPojo sellByToken = this.getSellRequestedWithMatchingToken(transactionToken);
        Long sellId = sellByToken.getBuyOrder();
        sellStepperService.setSellStatusToPaymentAborted(sellId);
      } else {
        this.processSellStatus(transactionToken);
      }
      URL resultPageUrl = new URL(paymentIntegrationService.getPaymentResultPageUrl() + "/" + transactionToken);
      return resultPageUrl.toURI();
    } catch (MalformedURLException | URISyntaxException ex) {
      logger.error("Malformed final URL for payment method; make sure this property is correctly configured.", ex);
      throw new RuntimeException("Transaction was confirmed, but server had an unexpected malfunction");
    }
  }

  @Override
  public SellPojo getResultingTransaction(String transactionToken) throws NotFoundException {
    Map<String, String> tokenMatcher = Maps.of("token", transactionToken).build();
    Predicate withMatchingToken = salesPredicateService.parseMap(tokenMatcher);
    return salesCrudService.readOne(withMatchingToken);
  }

  /**
   * Finds a transaction by its token, fetches the result of its payment and updates it in the database.
   * @param transactionToken A token provided by the payment integration service.
   * @throws NotFoundException If no transaction has a matching token.
   * @throws PaymentServiceException As raised at integration level.
   */
  private void processSellStatus(
    String transactionToken
  ) throws NotFoundException, PaymentServiceException {
    logger.trace("Looking up transaction with token={}...", transactionToken);
    SellPojo sellByToken = this.getSellRequestedWithMatchingToken(transactionToken);
    int statusCode = paymentIntegrationService.requestPaymentResult(transactionToken);
    Long sellId = sellByToken.getBuyOrder();
    logger.debug("Transaction found; updating sell status for id={}...", sellId);
    if (statusCode != 0) {
      logger.trace("Status code for transaction={}, means 'failed'", statusCode);
      sellStepperService.setSellStatusToPaymentFailed(sellId);
    } else {
      logger.trace("Status code for transaction={}, means 'success'", statusCode);
      sellStepperService.setSellStatusToPaidUnconfirmed(sellId);
    }
    logger.trace("Updating sell status: Done");
  }

  private SellPojo getSellRequestedWithMatchingToken(String transactionToken) throws NotFoundException {
    Map<String, String> startedWithTokenMatcher = Maps.of("statusName", "Payment Started").
                                                       and("token", transactionToken).build();
    Predicate startedTransactionWithMatchingToken = salesPredicateService.parseMap(startedWithTokenMatcher);
    return salesCrudService.readOne(startedTransactionWithMatchingToken);
  }

}
