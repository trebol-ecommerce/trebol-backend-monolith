package org.trebol.api.controllers;

import java.util.Objects;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.api.IProfileService;
import org.trebol.security.IAuthorizationHeaderParserService;

@RestController
@RequestMapping("/account/profile")
public class ProfileController {

  private final IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final ConversionService conversionService;
  private final IProfileService userProfileService;

  @Autowired
  public ProfileController(IAuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      ConversionService conversionService,
      IProfileService userProfileService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.conversionService = conversionService;
    this.userProfileService = userProfileService;
  }

  @GetMapping({"", "/"})
  public PersonPojo getProfile(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);

    if (authorizationHeader != null) {
      Claims body = jwtClaimsParserService.parseToken(authorizationHeader);

      String username = body.getSubject();
      Person personByUserName = userProfileService.getProfileFromUserName(username);
      if (personByUserName != null) {
        PersonPojo target = conversionService.convert(personByUserName, PersonPojo.class);
        return target;
      }
    } else {
      throw new RuntimeException("No authorization header was found");
    }
    return null;
  }

  @PutMapping({"", "/"})
  public boolean updateProfile(@RequestHeader HttpHeaders requestHeaders, @RequestBody PersonPojo newProfile) {

    // first retrieve profile with method above, validating token and what else
    PersonPojo currentProfile = getProfile(requestHeaders);

    // compare fetched id to input
    if (currentProfile != null && Objects.equals(currentProfile.getId(), newProfile.getId())) {
      Person target = conversionService.convert(newProfile, Person.class);
      return userProfileService.updateProfile(target);
    } else {
      return false;
    }
  }
}
