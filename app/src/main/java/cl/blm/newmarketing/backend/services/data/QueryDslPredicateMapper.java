package cl.blm.newmarketing.backend.services.data;

import java.util.Map;

import com.querydsl.core.types.Predicate;

/**
 * An interface for implementing translations from Maps to Predicates.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface QueryDslPredicateMapper {

  /**
   * Generates filtering conditions based on the provided Map data.
   *
   * @param queryParamsMap A map of keys and values, both being strings.
   *
   * @return A Predicate representing filtering conditions.
   */
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap);
}
