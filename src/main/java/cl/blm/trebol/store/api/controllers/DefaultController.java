package cl.blm.trebol.store.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
public class DefaultController {

  @GetMapping({"", "/"})
  public ResponseEntity defaultMapping() {
    return ResponseEntity.ok().build();
  }
}
