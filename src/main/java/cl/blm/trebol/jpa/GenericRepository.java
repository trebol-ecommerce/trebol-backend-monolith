package cl.blm.trebol.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Interface for JPA repositories with QueryDSL support.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <T> The entity class
 * @param <I> The identifier class
 */
public interface GenericRepository<T extends GenericEntity<I>, I>
    extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

}
