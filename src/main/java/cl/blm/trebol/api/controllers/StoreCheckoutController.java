package cl.blm.trebol.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutResponsePojo;
import cl.blm.trebol.api.pojo.WebpayCheckoutRequestPojo;
import cl.blm.trebol.config.CheckoutConfig;
import cl.blm.trebol.services.exposed.CheckoutService;
import cl.blm.trebol.services.security.AuthorizationHeaderParserService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/store/checkout")
public class StoreCheckoutController {

  private final CheckoutService checkoutService;
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final CheckoutConfig checkoutConfig;

  @Autowired
  public StoreCheckoutController(
      CheckoutService checkoutService,
      AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      CheckoutConfig checkoutConfig) {
    this.checkoutService = checkoutService;
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.checkoutConfig = checkoutConfig;
  }

  /**
   * Save a new transaction, forward request to checkout server, and save the generated token for later validation
   *
   * @param httpHeaders
   * @param cartDetails The checkout details (e.g. the items to be purchased)
   * @return
   */
  @PostMapping("")
  public WebpayCheckoutResponsePojo submitCart(
      @RequestHeader HttpHeaders httpHeaders,
      @RequestBody Collection<SellDetailPojo> cartDetails) {
    String authorization = jwtClaimsParserService.extractAuthorizationHeader(httpHeaders);
    WebpayCheckoutRequestPojo savedCartTransactionRequest = checkoutService.saveCartAsTransactionRequest(authorization, cartDetails);
    WebpayCheckoutResponsePojo transactionRedirect = checkoutService.startWebpayTransaction(savedCartTransactionRequest);
    return transactionRedirect;
  }

  /**
   * Validate the status of a pending transaction and save the resulting metadata
   *
   * @param transactionFormData
   * @return
   */
  @PostMapping("/validate")
  public ResponseEntity<Void> validateTransaction(@RequestBody MultiValueMap<String, String> transactionFormData) {
    String tokenWs = transactionFormData.getFirst(checkoutConfig.getTransactionTokenPostDataKey());
    String returnUrl;
    try {
      checkoutService.confirmWebpayTransactionResult(tokenWs);
      returnUrl = checkoutConfig.getSuccessPageURL();
    } catch (Exception exc) {
      returnUrl = checkoutConfig.getFailurePageURL();
    }
    return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", returnUrl).build();
  }
}
