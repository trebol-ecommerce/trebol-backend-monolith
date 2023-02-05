/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.services.ProfileService;
import org.trebol.common.exceptions.BadInputException;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@RestController
@RequestMapping("/account/profile")
@PreAuthorize("isAuthenticated()")
public class AccountProfileController {
  private final ProfileService userProfileService;

  @Autowired
  public AccountProfileController(
    ProfileService userProfileService
  ) {
    this.userProfileService = userProfileService;
  }

  @GetMapping({"", "/"})
  public PersonPojo getProfile(Principal principal)
    throws EntityNotFoundException {
    String username = principal.getName();
    return userProfileService.getProfileFromUserName(username);
  }

  @PutMapping({"", "/"})
  public void updateProfile(Principal principal, @RequestBody PersonPojo newProfile)
    throws EntityNotFoundException, BadInputException {
    String username = principal.getName();
    userProfileService.updateProfileForUserWithName(username, newProfile);
  }
}
