package cl.blm.newmarketing.store.api.controllers;

import java.util.Collection;

import io.jsonwebtoken.Claims;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.store.api.pojo.AuthorizedAccessPojo;
import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.jpa.entities.Person;
import cl.blm.newmarketing.store.services.UserProfileService;
import cl.blm.newmarketing.store.services.security.AuthorizationTokenParserService;

@RestController
public class ProfileController {

  private final AuthorizationTokenParserService<Claims> jwtClaimsParserService;
  private final ConversionService conversionService;
  private final UserProfileService userProfileService;
  private final UserDetailsService userDetailsService;

  @Autowired
  public ProfileController(AuthorizationTokenParserService<Claims> jwtClaimsParserService,
      ConversionService conversionService,
      UserProfileService userProfileService,
      UserDetailsService userDetailsService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.conversionService = conversionService;
    this.userProfileService = userProfileService;
    this.userDetailsService = userDetailsService;
  }

  @GetMapping("/profile")
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

  @PutMapping("/profile")
  public boolean updateProfile(@RequestHeader HttpHeaders requestHeaders, @RequestBody PersonPojo newProfile) {

    // first retrieve profile with method above, validating token and what else
    PersonPojo currentProfile = getProfile(requestHeaders);

    // compare fetched id to input
    if (currentProfile != null && currentProfile.getId() == newProfile.getId()) {
      Person target = conversionService.convert(newProfile, Person.class);
      return userProfileService.updateProfile(target);
    } else {
      return false;
    }
  }

  @GetMapping("/validate")
  public boolean validateToken(@RequestHeader HttpHeaders requestHeaders) {
    try {
      String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);
      jwtClaimsParserService.parseToken(authorizationHeader);
      return true;
    } catch (Exception e) {
      LoggerFactory.getLogger(ProfileController.class).warn("Could not validate token", e);
      return false;
    }
  }

  @GetMapping("/routes")
  public AuthorizedAccessPojo getAuthorizedAccess(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);

    if (authorizationHeader != null) {
      Claims body = jwtClaimsParserService.parseToken(authorizationHeader);
      String username = body.getSubject();
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
      AuthorizedAccessPojo target = conversionService.convert(authorities, AuthorizedAccessPojo.class);
      return target;
    }
    return null;
  }
}
