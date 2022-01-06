package org.trebol.jpa.services;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.pojo.DataPagePojo;


/**
 * Interface for wrapping basic CRUD service operations by using Pojo classes
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The pojo type class
 * @param <I> The identifier type class
 */
public interface ICrudJpaService<P, I> {

  /**
   * Inserts and persists an item.
   *
   * @param dto The item to be created.
   *
   * @return The created item, with updated properties (most importantly its ID),
   *         or null if the item could not be created.
   * @throws org.trebol.exceptions.BadInputException When the data in the input object is not valid or is insufficient.
   * @throws org.trebol.exceptions.EntityAlreadyExistsException When the data collides with an existing registry.
   */
  P create(P dto) throws BadInputException, EntityAlreadyExistsException;

  /**
   * Queries a paged collection of items.
   *
   * @param pageSize  Number of items per page.
   * @param pageIndex Page index (0-based).
   * @param order     Sorting order specification
   * @param filters   Filtering conditions
   *
   * @return The requested page of items along some metadata
   */
  DataPagePojo<P> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters);

  /**
   * Retrieves the first item that matches a certain filter.
   *
   * @param filters   Filtering conditions
   *
   * @return The requested item
   * @throws javassist.NotFoundException When no item matches the filter.
   */
  P readOne(Predicate filters) throws NotFoundException;

  /**
   * Updates an existing item.
   *
   * @param dto The item to be updated. Its identifying field may or may not be
   *            present, and can be different from the second method param.
   *
   * @return The saved item, with updated properties
   * @throws javassist.NotFoundException When no item matches the given item
   * @throws org.trebol.exceptions.BadInputException When the data in the input object is not valid.
   */
  P update(P dto) throws NotFoundException, BadInputException;

  /**
   * Updates an existing item matching given filtering conditions
   *
   * @param dto The item with upcoming data.
   * @param filters Filtering conditions
   *
   * @return The saved item, with updated properties
   * @throws javassist.NotFoundException When no item matches given filters.
   * @throws org.trebol.exceptions.BadInputException When the data in the input object is not valid.
   */
  P update(P dto, Predicate filters) throws NotFoundException, BadInputException;

  /**
   * Deletes all items matching given filtering conditions.
   *
   * @param filters Filtering conditions
   *
   * @throws javassist.NotFoundException When no item matches given filters.
   */
  void delete(Predicate filters) throws NotFoundException;
}
