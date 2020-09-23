package cl.blm.newmarketing.store.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Maps;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.api.pojo.UserPojo;
import cl.blm.newmarketing.store.jpa.entities.Person;
import cl.blm.newmarketing.store.jpa.entities.User;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;
import cl.blm.newmarketing.store.services.security.AuthorizationTokenParserService;

@RestController
public class ProfileController {

  private final AuthorizationTokenParserService<Claims> jwtClaimsParserService;
  private final ConversionService conversionService;
  private final GenericEntityCrudService<UserPojo, User, Integer> crudService;

  @Autowired
  public ProfileController(AuthorizationTokenParserService<Claims> jwtClaimsParserService,
      ConversionService conversionService,
      GenericEntityCrudService<UserPojo, User, Integer> crudService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.conversionService = conversionService;
    this.crudService = crudService;
  }

  @GetMapping("/profile")
  public PersonPojo getProfile(@RequestHeader HttpHeaders requestHeaders) {
    String authorizationHeader = jwtClaimsParserService.extractAuthorizationHeader(requestHeaders);

    if (authorizationHeader != null) {
      Claims body = jwtClaimsParserService.parseToken(authorizationHeader);

      String username = body.getSubject();
      Predicate pr = crudService.queryParamsMapToPredicate(Maps.of("name", username).build());
      Page<User> usersByName = crudService.getAllEntities(Pageable.unpaged(), pr);
      if (usersByName.hasContent()) {
        Person personByUserName = usersByName.getContent().get(0).getPerson();
        PersonPojo target = conversionService.convert(personByUserName, PersonPojo.class);
        return target;
      }
    } else {
      throw new RuntimeException("No authorization header was found");
    }
    return null;
  }
}
