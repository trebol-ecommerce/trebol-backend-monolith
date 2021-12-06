package org.trebol.operation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
public class RootController {

  @GetMapping({"", "/"})
  public void defaultMapping() {
    /* ping-like method. return success status without message body */
  }
}
