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
import org.trebol.api.models.DataPagePojo;
import org.trebol.common.exceptions.BadInputException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;


/**
 * Performs all four CRUD operations against a given type of data
 *
 * @param <M> The signature model class
 * @param <E> The signature entity class
 */
public interface CrudService<M, E> {

  /**
   * Saves a registry into the persistence context.
   *
   * @param input The model to be added to the persistence context.
   * @return The model as inserted, with updated properties (usually its <b>id</b> field).
   * @throws BadInputException     When the data required from the input object is invalid or insufficient.
   * @throws EntityExistsException When the data is a duplicate of an existing registry.
   */
  M create(M input) throws BadInputException, EntityExistsException;

  /**
   * Queries a paged collection of registries in the form of a {@link org.trebol.api.models.DataPagePojo}.
   *
   * @param pageSize  Number of items per page.
   * @param pageIndex Page index (0-based).
   * @param order     Sorting order specification
   * @param filters   Filtering conditions
   * @return The requested page of items along some metadata
   */
  DataPagePojo<M> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters);

  // TODO why throw an exception when no match is made? That is not an application error - consider using Optional<M> as return type and ditch the throws clause.
  /**
   * Retrieves the first item that matches certain filtering conditions.
   * These are wrapped in a {@link com.querydsl.core.types.Predicate}, and it is usually the task of the
   * {@link org.trebol.jpa.services.PredicateService} to create the Predicate for it in the first place.
   *
   * @param filters Filtering conditions
   * @return The requested item
   * @throws EntityNotFoundException When no item matches the filter.
   */
  M readOne(Predicate filters) throws EntityNotFoundException;

  // TODO why does only this method return an entity type? Perhaps it can be refactored away.
  /**
   * Attempts to match the given model class instance to an existing entity, by querying the
   * persistence context using its <i>identifying property</i>.<br/>
   * Note that the <i>identifying property</i> depends on the data type.
   *
   * @param example An instance of the model class. It should hold a valid  <i>identification property</i>
   *                that can be matched against in the persistence context.
   * @return A container possibly holding an entity.
   * @throws BadInputException When the model does not have a valid <i>identification property</i>.
   */
  Optional<E> getExisting(M example) throws BadInputException;

  // TODO in implementations, this is more akin to a PATCH method, in that it partially updates the data, than a PUT method, which intent is to updates the whole model. This interface should declare separate methods for doing both things, and make use of different POJOs to validate data accordingly.
  /**
   * Updates an existing registry, first fetching it from its <i>identifying property</i> and
   * then replacing the rest of its contents with those from the input model.<br/>
   * Note that the <i>identifying property</i> depends on the data type.<br/>
   * <br/>
   * <i>NOTE: This intended behavior may change in the future.</i>
   *
   * @param input The model to be updated. Its identifying field may or may not be
   *              present, and can be different from the second method param.
   * @return A model-copy of the saved registry, with its properties updated accordingly.
   * @throws EntityNotFoundException When no registry matches the given input.
   * @throws BadInputException       When the data in the input object is not valid.<br/>
   *                                 It is expected that some portions data may be null, because it may not have
   *                                 been included during serialization. Such cases are <i>not</i> meant to cause
   *                                 a BadInputException.<br/>
   */
  M update(M input) throws EntityNotFoundException, BadInputException;

  // TODO in implementations, this is more akin to a PATCH method, in that it partially updates the data, than a PUT method, which intent is to updates the whole model. This interface should declare separate methods for doing both things, and make use of different POJOs to validate data accordingly.
  /**
   * Updates one or several existing registries, by first targeting them
   * given some filtering conditions wrapped in a {@link com.querydsl.core.types.Predicate}.<br/>
   * It is usually the task of the {@link org.trebol.jpa.services.PredicateService}
   * to create this Predicate in the first place.
   * <br/>
   * <i>NOTE: This intended behavior may change in the future.</i>
   *
   * @param input   The model with upcoming data.
   * @param filters The QueryDSL filtering conditions
   * @return The saved item, with updated properties
   * @throws EntityNotFoundException When no item matches given filters.
   * @throws BadInputException       When the data in the input object is not valid.
   */
  M update(M input, Predicate filters) throws EntityNotFoundException, BadInputException;

  // TODO why throw an exception? It is not an application error to not delete any registry. Consider changing the return type to aptly inform the caller about the result.
  /**
   * Deletes all items matching given filtering conditions wrapped in a
   * {@link com.querydsl.core.types.Predicate}.<br/>
   * It is usually the task of the {@link org.trebol.jpa.services.PredicateService}
   * to create this Predicate in the first place.
   *
   * @param filters Filtering conditions
   * @throws EntityNotFoundException When no item matches given filters.
   */
  void delete(Predicate filters) throws EntityNotFoundException;
}
