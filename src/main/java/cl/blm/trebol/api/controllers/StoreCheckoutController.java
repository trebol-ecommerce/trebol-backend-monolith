package cl.blm.trebol.api.controllers;

import java.util.Collection;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.jpa.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/store/checkout")
public class StoreCheckoutController {

  // TODO Implement this
  @PostMapping("")
  public Object submitCart(@RequestBody Collection<SellDetail> cartDetails) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  // TODO Implement this
  @PostMapping("/validate")
  public Object validateTransaction(@RequestBody Object transactionIdentifier) {
    throw new UnsupportedOperationException("Method not implemented");
  }
}
