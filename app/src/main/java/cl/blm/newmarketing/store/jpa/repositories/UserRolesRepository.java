package cl.blm.newmarketing.store.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.store.jpa.GenericRepository;
import cl.blm.newmarketing.store.jpa.entities.UserRole;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UserRolesRepository
    extends GenericRepository<UserRole, Integer> {

}
