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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.api.pojo.WebpayTransactionPojo;
import cl.blm.trebol.config.CheckoutConfig;
import cl.blm.trebol.services.exposed.CheckoutService;
import cl.blm.trebol.services.security.AuthenticatedPeopleService;
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

  @PostMapping("")
  public WebPayRedirectionData submitCart(
      @RequestHeader HttpHeaders httpHeaders,
      @RequestBody Collection<SellDetailPojo> cartDetails) {
    String authorization = jwtClaimsParserService.extractAuthorizationHeader(httpHeaders);
    WebpayTransactionPojo savedCartTransactionRequest = checkoutService.saveCartAsTransactionRequest(authorization, cartDetails);
    WebPayRedirectionData transactionRedirect = checkoutService.startWebpayTransaction(savedCartTransactionRequest);
    return transactionRedirect;
  }

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
