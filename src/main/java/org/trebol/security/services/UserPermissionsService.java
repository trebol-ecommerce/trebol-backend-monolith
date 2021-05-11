package org.trebol.security.services;

import java.util.Set;

import org.trebol.jpa.entities.Permission;
import org.trebol.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface UserPermissionsService {
  Set<Permission> loadPermissionsForUser(User source);
}
