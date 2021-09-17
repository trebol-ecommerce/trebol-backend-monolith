package org.trebol.operation.controllers;

import java.security.Principal;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.operation.IProfileService;
import org.trebol.pojo.PersonPojo;
import org.trebol.exceptions.BadInputException;

@RestController
@RequestMapping("/account/profile")
public class AccountProfileController {

  private final IProfileService userProfileService;

  @Autowired
  public AccountProfileController(IProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @GetMapping({"", "/"})
  public PersonPojo getProfile(Principal principal) throws NotFoundException {
    String username = principal.getName();
    return userProfileService.getProfileFromUserName(username);
  }

  @PutMapping({"", "/"})
  public void updateProfile(Principal principal, @RequestBody PersonPojo newProfile)
          throws NotFoundException, BadInputException {
    String username = principal.getName();
    userProfileService.updateProfileForUserWithName(principal.getName(), newProfile);
  }
}
