package cl.blm.newmarketing.store.services.security;

import java.util.Set;

import cl.blm.newmarketing.store.jpa.entities.Permission;
import cl.blm.newmarketing.store.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface UserPermissionsService {
  Set<Permission> loadPermissionsForUser(User source);
}
