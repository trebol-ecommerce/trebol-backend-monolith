/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.pojo.DataPagePojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;


/**
 * Interface for wrapping basic CRUD service operations by using Pojo classes
 *
 * @param <P> The pojo type class
 */
public interface CrudService<P, E> {

  /**
   * Inserts and persists an item.
   *
   * @param dto The item to be created.
   * @return The created item, with updated properties (most importantly its ID),
   * or null if the item could not be created.
   * @throws BadInputException When the data in the input object is not valid or is insufficient.
   * @throws EntityExistsException                   When the data collides with an existing registry.
   */
  P create(P dto) throws BadInputException, EntityExistsException;

  /**
   * Queries a paged collection of items.
   *
   * @param pageSize  Number of items per page.
   * @param pageIndex Page index (0-based).
   * @param order     Sorting order specification
   * @param filters   Filtering conditions
   * @return The requested page of items along some metadata
   */
  DataPagePojo<P> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters);

  /**
   * Retrieves the first item that matches a certain filter.
   *
   * @param filters Filtering conditions
   * @return The requested item
   * @throws EntityNotFoundException When no item matches the filter.
   */
  P readOne(Predicate filters) throws EntityNotFoundException;

  /**
   * Attempts to match the given pojo class instance to an existing entity.
   * This method is also useful to assert bare-minimum pojo validity for using it to update data.
   *
   * @param example The pojo class instance that should hold a valid identifying property
   * @return A possible entity match that may have succeeded or not
   * @throws BadInputException When the pojo doesn't have its identifying property.
   */
  Optional<E> getExisting(P example) throws BadInputException;

  /**
   * Updates an existing item.
   *
   * @param dto The item to be updated. Its identifying field may or may not be
   *            present, and can be different from the second method param.
   * @return The saved item, with updated properties
   * @throws EntityNotFoundException                 When no item matches the given item
   * @throws BadInputException When the data in the input object is not valid.
   */
  P update(P dto) throws EntityNotFoundException, BadInputException;

  /**
   * Updates an existing item matching given filtering conditions
   *
   * @param dto     The item with upcoming data.
   * @param filters Filtering conditions
   * @return The saved item, with updated properties
   * @throws EntityNotFoundException                 When no item matches given filters.
   * @throws BadInputException When the data in the input object is not valid.
   */
  P update(P dto, Predicate filters) throws EntityNotFoundException, BadInputException;

  /**
   * Deletes all items matching given filtering conditions.
   *
   * @param filters Filtering conditions
   * @throws EntityNotFoundException When no item matches given filters.
   */
  void delete(Predicate filters) throws EntityNotFoundException;
}
