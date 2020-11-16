package cl.blm.trebol.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.ClientPojo;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellDetailPojo;
import cl.blm.trebol.api.pojo.SellPojo;
import cl.blm.trebol.api.pojo.WebPayRedirectionData;
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
  private final AuthenticatedPeopleService authenticatedPeopleService;
  private final ClientPersonRelationService clientPersonRelationService;

  @Autowired
  public StoreCheckoutController(CheckoutService checkoutService,
      AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      AuthenticatedPeopleService authenticatedPeopleService,
      ClientPersonRelationService clientPersonRelationService) {
    this.checkoutService = checkoutService;
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.authenticatedPeopleService = authenticatedPeopleService;
    this.clientPersonRelationService = clientPersonRelationService;
  }

  private int fetchClientId(HttpHeaders httpHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(httpHeaders);
    PersonPojo authenticatedPerson = authenticatedPeopleService.fetchAuthenticatedUserPersonProfile(authorizationHeader);
    int personId = authenticatedPerson.getId();
    ClientPojo authenticatedClient = clientPersonRelationService.getClientFromPersonId(personId);
    int clientId = authenticatedClient.getId();
    return clientId;
  }

  @PostMapping("")
  public WebPayRedirectionData submitCart(
      @RequestHeader HttpHeaders httpHeaders,
      @RequestBody Collection<SellDetailPojo> cartDetails) {
    int clientId = fetchClientId(httpHeaders);
    SellPojo savedCartTransactionRequest = checkoutService.saveCartAsTransactionRequest(clientId, cartDetails);
    WebPayRedirectionData transactionRedirect = checkoutService.startWebpayTransaction(savedCartTransactionRequest);
    return transactionRedirect;
  }

  // TODO Implement this
  @PostMapping("/validate")
  public Object validateTransaction(@RequestBody Object transactionIdentifier) {
    throw new UnsupportedOperationException("Method not implemented");
  }
}
