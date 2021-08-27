package org.trebol.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.RegistrationPojo;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.api.IRegistrationService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@RestController
@RequestMapping("/public/register")
public class PublicRegisterController {

  private final IRegistrationService registrationService;

  @Autowired
  public PublicRegisterController(IRegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping({"", "/"})
  public void register(
    @RequestBody RegistrationPojo userProfile
  ) throws EntityAlreadyExistsException {
    this.registrationService.register(userProfile);
  }
}
