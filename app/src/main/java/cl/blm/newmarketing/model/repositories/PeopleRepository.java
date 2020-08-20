package cl.blm.newmarketing.model.repositories;

import cl.blm.newmarketing.model.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository 
public interface PeopleRepository
    extends JpaRepository<Person, Integer>, QuerydslPredicateExecutor<Person>{
    
}
