package cl.blm.trebol.api.controllers;

import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
import cl.blm.trebol.api.pojo.WebpayTransactionPojo;
import cl.blm.trebol.services.exposed.CheckoutService;
import cl.blm.trebol.services.security.AuthenticatedPeopleService;
import cl.blm.trebol.services.security.AuthorizationHeaderParserService;
import cl.blm.trebol.services.user.ClientPersonRelationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/store/checkout")
public class StoreCheckoutController {

  private final CheckoutService checkoutService;
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  @Autowired
  public StoreCheckoutController(CheckoutService checkoutService,
      AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      AuthenticatedPeopleService authenticatedPeopleService,
      ClientPersonRelationService clientPersonRelationService) {
    this.checkoutService = checkoutService;
    this.jwtClaimsParserService = jwtClaimsParserService;
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
  public ResponseEntity<Void> validateTransaction(@RequestBody Map<String, String> transactionFormData) {
//    TODO: formData should contain a token_ws that can be matched to a saved database transaction
//    ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", headerValues)
    return ResponseEntity.ok().build();
  }
}
