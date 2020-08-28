package cl.blm.newmarketing.backend.model.repositories;

import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.backend.model.GenericRepository;
import cl.blm.newmarketing.backend.model.entities.Person;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface PeopleRepository
    extends GenericRepository<Person, Integer> {

}
