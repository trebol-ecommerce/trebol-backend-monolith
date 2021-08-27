package org.trebol.jpa;

import org.springframework.lang.Nullable;
import org.trebol.api.DataPage;

import javassist.NotFoundException;


/**
 * Interface for implementing the basic CRUD service operations.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <T> The items type class.
 * @param <I> The identifier type class.
 * @param <F> The filter type class.
 */
public interface IJpaCrudService<T, I, F>
  extends ICrudService<T, I>, IQueryDslPredicateParserService {

  /**
   * Queries a paged collection of items.
   *
   * @param pageSize  Number of items per page.
   * @param pageIndex Page index (0-based).
   * @param filters   Filtering conditions
   *
   * @return The requested collection of items. May be zero-sized.
   */
  public DataPage<T> readMany(int pageSize, int pageIndex, @Nullable F filters);

  /**
   * Retrieves the first item that matches a certain filter.
   *
   * @param filters   Filtering conditions
   *
   * @return The requested item
   * @throws javassist.NotFoundException
   */
  public T readOne(F filters) throws NotFoundException;
}
