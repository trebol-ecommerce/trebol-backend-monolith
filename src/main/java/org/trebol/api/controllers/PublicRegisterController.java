package org.trebol.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.jpa.exceptions.PersonAlreadyExistsException;
import org.trebol.jpa.exceptions.UserAlreadyExistsException;
import org.trebol.api.services.RegistrationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/public/register")
public class PublicRegisterController {

  private final RegistrationService registrationService;

  @Autowired
  public PublicRegisterController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping({"", "/"})
  public void register(
    @RequestBody RegistrationPojo userProfile
  ) throws UserAlreadyExistsException, PersonAlreadyExistsException {
    this.registrationService.register(userProfile);
  }
}
