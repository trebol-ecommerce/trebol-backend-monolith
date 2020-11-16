package cl.blm.trebol.services.security.impl;

import java.util.Optional;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.jpa.entities.User;
import cl.blm.trebol.jpa.repositories.UsersRepository;
import cl.blm.trebol.services.security.AuthenticatedPeopleService;
import cl.blm.trebol.services.security.AuthorizationHeaderParserService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
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

  private User getAuthenticatedUser(String authorizationHeader) throws UsernameNotFoundException {
    Claims body = jwtClaimsParserService.parseToken(authorizationHeader);
    String username = body.getSubject();
    Optional<User> foundUser = usersRepository.findByNameWithProfile(username);
    if (foundUser.isPresent()) {
      return foundUser.get();
    }
    throw new UsernameNotFoundException(username);
  }

  @Override
  public PersonPojo fetchAuthenticatedUserPersonProfile(String authorizationHeader) {
    User authenticatedUser = getAuthenticatedUser(authorizationHeader);
    Person authenticatedPerson = authenticatedUser.getPerson();
    PersonPojo target = conversionService.convert(authenticatedPerson, PersonPojo.class);
    return target;
  }

}
