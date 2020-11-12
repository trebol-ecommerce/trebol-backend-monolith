package cl.blm.trebol.jpa.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.trebol.jpa.GenericRepository;
import cl.blm.trebol.jpa.entities.UserRole;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface UserRolesRepository
    extends GenericRepository<UserRole, Integer> {

}
