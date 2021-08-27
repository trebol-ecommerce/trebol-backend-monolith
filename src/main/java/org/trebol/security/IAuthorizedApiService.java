package org.trebol.security;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IAuthorizedApiService {
  public Collection<String> getAuthorizedApiRoutes(UserDetails userDetails);

  public Collection<String> getAuthorizedApiRouteAccess(UserDetails userDetails, String apiRoute);
}
