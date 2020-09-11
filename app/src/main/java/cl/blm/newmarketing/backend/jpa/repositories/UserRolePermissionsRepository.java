package cl.blm.newmarketing.backend.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.backend.jpa.GenericRepository;
import cl.blm.newmarketing.backend.jpa.entities.UserRolePermission;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UserRolePermissionsRepository
    extends GenericRepository<UserRolePermission, Integer> {

}
