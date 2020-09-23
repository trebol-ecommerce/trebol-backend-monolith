package cl.blm.newmarketing.store.api.controllers;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.jpa.entities.Person;
import cl.blm.newmarketing.store.services.UserProfileService;
import cl.blm.newmarketing.store.services.security.AuthorizationTokenParserService;

@RestController
public class ProfileController {

  private final AuthorizationTokenParserService<Claims> jwtClaimsParserService;
  private final ConversionService conversionService;
  private final UserProfileService userCrudService;

  @Autowired
  public ProfileController(AuthorizationTokenParserService<Claims> jwtClaimsParserService,
      ConversionService conversionService,
      UserProfileService crudService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.conversionService = conversionService;
    this.userCrudService = crudService;
  }

  @GetMapping("/profile")
  public PersonPojo getProfile(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);

    if (authorizationHeader != null) {
      Claims body = jwtClaimsParserService.parseToken(authorizationHeader);

      String username = body.getSubject();
      Optional<Person> personByUserName = userCrudService.getProfileFromUserName(username);
      if (personByUserName.isPresent()) {
        Person source = personByUserName.get();
        PersonPojo target = conversionService.convert(source, PersonPojo.class);
        return target;
      }
    } else {
      throw new RuntimeException("No authorization header was found");
    }
    return null;
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
}
