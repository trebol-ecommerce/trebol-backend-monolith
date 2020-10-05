package cl.blm.trebol.store.services.security;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface AuthorizedApiService {
  public Collection<String> getAuthorizedApiRoutes(UserDetails userDetails);

  public Collection<String> getAuthorizedApiRouteAccess(UserDetails userDetails, String apiRoute);
}
