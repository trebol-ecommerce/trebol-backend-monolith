package cl.blm.trebol.services.security;

import cl.blm.trebol.api.pojo.PersonPojo;

/**
 * Service interface that can find a Person profile behind an authenticated user by reading auth information
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface AuthenticatedPeopleService {
  PersonPojo fetchAuthenticatedUserPersonProfile(String authorizationHeader);
}
