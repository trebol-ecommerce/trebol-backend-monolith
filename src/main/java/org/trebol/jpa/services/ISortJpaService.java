package org.trebol.jpa.services;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * An interface for supporting custom algorithms to parse Sort orders for use in JPA queries.
 * This should only be implemented and used in cases not covered by the GenericDataController
 * @param <E> The target Entity class
 */
public interface ISortJpaService<E> {

  /**
   * Get base QueryDSL type for building OrderSpecifiers
   */
  EntityPathBase<E> getBasePath();

  /**
   * Reads Map and creates sort order in accordance to its data.
   *
   * @param queryParamsMap A map of keys and values
   * @return A Sort order as parsed from the input map. May be null if the input is invalid
   */
  @Nullable
  Sort parseMap(Map<String, String> queryParamsMap);
}
