package cl.blm.trebol.api.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.trebol.api.pojo.UserPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/register")
public class RegistrationController {

  @PostMapping
  public boolean register(@RequestBody UserPojo userProfile) {
    throw new UnsupportedOperationException();
  }
}
