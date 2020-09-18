package cl.blm.newmarketing.store.api.controllers;

import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Maps;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.api.pojo.UserPojo;
import cl.blm.newmarketing.store.config.JwtProperties;
import cl.blm.newmarketing.store.jpa.entities.Person;
import cl.blm.newmarketing.store.jpa.entities.User;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

@RestController
public class ProfileController {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;
  private final ConversionService conversionService;
  private final GenericEntityCrudService<UserPojo, User, Integer> crudService;

  @Autowired
  public ProfileController(SecretKey secretKey, JwtProperties jwtProperties,
      ConversionService conversionService,
      GenericEntityCrudService<UserPojo, User, Integer> crudService) {
    this.secretKey = secretKey;
    this.jwtProperties = jwtProperties;
    this.conversionService = conversionService;
    this.crudService = crudService;
  }

  @GetMapping("/profile")
  public PersonPojo getProfile(@RequestHeader Map<String, String> requestHeaders) {
    String authHeaderKey = jwtProperties.getAuthorizationHeader();
    if (requestHeaders.containsKey(authHeaderKey)) {
      String authorizationHeader = requestHeaders.get(authHeaderKey);

      String token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");

      try {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        String username = body.getSubject();
        Predicate pr = crudService.queryParamsMapToPredicate(Maps.of("name", username).build());
        Page<User> usersByName = crudService.getAllEntities(Pageable.unpaged(), pr);
        if (usersByName.hasContent()) {
          Person personByUserName = usersByName.getContent().get(0).getPerson();
          PersonPojo target = conversionService.convert(personByUserName, PersonPojo.class);
          return target;
        }
      } catch (JwtException e) {
        throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
      }
    }
    return null;
  }
}
