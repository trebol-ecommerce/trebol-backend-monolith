package cl.blm.trebol.store.services.security;

import java.util.Set;

import cl.blm.trebol.store.jpa.entities.Permission;
import cl.blm.trebol.store.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface UserPermissionsService {
  Set<Permission> loadPermissionsForUser(User source);
}
