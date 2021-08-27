package org.trebol.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Interface for JPA repositories with QueryDSL support.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <E> The entity class
 */
public interface IJpaRepository<E>
    extends JpaRepository<E, Long>, QuerydslPredicateExecutor<E> {

}
