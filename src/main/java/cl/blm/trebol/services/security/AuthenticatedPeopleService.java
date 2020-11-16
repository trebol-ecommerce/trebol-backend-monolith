package cl.blm.trebol.services.security;

import cl.blm.trebol.jpa.entities.Person;

/**
 * Service interface that can find a Person profile behind an authenticated user by reading auth information
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface AuthenticatedPeopleService {
  Person fetchAuthenticatedUserPersonProfile(String authorizationHeader);
}
