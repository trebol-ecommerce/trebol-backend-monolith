package org.trebol.jpa.services;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;

import java.util.Map;

/**
 * An interface for parsing Predicates for use in JPA queries.
 * @param <E> The Entity class
 */
public interface IPredicateJpaService<E> {

  /**
   * Get base QueryDSL type for building Predicates
   */
  EntityPathBase<E> getBasePath();

  /**
   * Reads Map and creates filtering conditions based on its data.
   *
   * @param queryParamsMap A map of keys and values
   * @return A Predicate with filtering conditions as parsed from the input map
   */
  Predicate parseMap(Map<String, String> queryParamsMap);
}
