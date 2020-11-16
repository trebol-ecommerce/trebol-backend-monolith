package cl.blm.trebol.services.security.impl;

import java.util.Optional;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

  @Autowired
  public AuthenticatedPeopleServiceImpl(AuthorizationHeaderParserService<Claims> jwtClaimsParserService, UsersRepository usersRepository) {
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.usersRepository = usersRepository;
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
  public Person fetchAuthenticatedUserPersonProfile(String authorizationHeader) {
    User authenticatedUser = getAuthenticatedUser(authorizationHeader);
    Person authenticatedPerson = authenticatedUser.getPerson();
    return authenticatedPerson;
  }

}
