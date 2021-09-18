package org.trebol.security;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IAuthorizedApiService {
  Collection<String> getAuthorizedApiRoutes(UserDetails userDetails);

  Collection<String> getAuthorizedApiRouteAccess(UserDetails userDetails, String apiRoute);
}
