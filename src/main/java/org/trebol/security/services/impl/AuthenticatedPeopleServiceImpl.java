package org.trebol.security.services.impl;

import java.util.Optional;

import javax.annotation.Nullable;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.repositories.UsersRepository;
import org.trebol.security.services.AuthenticatedPeopleService;
import org.trebol.security.services.AuthorizationHeaderParserService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class AuthenticatedPeopleServiceImpl
    implements AuthenticatedPeopleService {

  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final UsersRepository usersRepository;
  private final ConversionService conversionService;

  @Autowired
  public AuthenticatedPeopleServiceImpl(AuthorizationHeaderParserService<Claims> jwtClaimsParserService, UsersRepository usersRepository, ConversionService conversionService) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.usersRepository = usersRepository;
    this.conversionService = conversionService;
  }

  @Nullable
  private User getAuthenticatedUser(String authorizationHeader) {
    Claims body = jwtClaimsParserService.parseToken(authorizationHeader);
    String username = body.getSubject();
    Optional<User> foundUser = usersRepository.findByNameWithProfile(username);
    if (foundUser.isPresent()) {
      return foundUser.get();
    } else {
      return null;
    }
  }

  @Nullable
  @Override
  public PersonPojo fetchAuthenticatedUserPersonProfile(String authorizationHeader) {
    User authenticatedUser = this.getAuthenticatedUser(authorizationHeader);
    if (authenticatedUser != null) {
      Person authenticatedPerson = authenticatedUser.getPerson();
      PersonPojo target = conversionService.convert(authenticatedPerson, PersonPojo.class);
      return target;
    } else {
      return null;
    }
  }

}
