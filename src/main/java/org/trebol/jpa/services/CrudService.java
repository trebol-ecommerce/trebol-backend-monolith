package org.trebol.jpa.services;

import org.springframework.lang.Nullable;
import org.trebol.api.DataPage;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;


/**
 * Interface for implementing the basic CRUD service operations.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <T> The items' type class.
 * @param <F> The filters' type class.
 */
public interface CrudService<T, F> {

  /**
   * Inserts and persists an item.
   *
   * @param dto The item to be created.
   *
   * @return The created item, with updated properties (most importantly its ID),
   *         or null if the item could not be created.
   * @throws org.trebol.jpa.exceptions.EntityAlreadyExistsException
   */
  @Nullable
  public T create(T dto) throws EntityAlreadyExistsException;

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
   * Retrieves an item by its ID.
   *
   * @param id The unique identifier of the item.
   *
   * @return The requested item, or null if it was not found.
   */
  @Nullable
  public T readOne(Long id);

  /**
   * Retrieves the first item that matches a certain filter.
   *
   * @param filters   Filtering conditions
   *
   * @return The requested item, or null if it was not found.
   */
  @Nullable
  public T readOne(F filters);

  /**
   * Updates an existing item.
   *
   * @param dto The item to be updated. Its identifying field may or may not be
   *            present, and can be different from the second method param.
   * @param id  The unique identifier of the item.
   *
   * @return The saved item, with updated properties, or null if the item was not
   *         found.
   */
  @Nullable
  public T update(T dto, Long id);

  /**
   * Finds an item by its ID and deletes it.
   *
   * @param id Its unique identifier.
   *
   * @return true on success, false otherwise.
   */
  public boolean delete(Long id);
}
