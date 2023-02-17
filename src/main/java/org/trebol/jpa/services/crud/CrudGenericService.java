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

package org.trebol.jpa.services.crud;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.DataPagePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.Repository;
import org.trebol.jpa.services.ConverterService;
import org.trebol.jpa.services.CrudService;
import org.trebol.jpa.services.PatchService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstraction that supports all four CRUD operations.<br/>
 * Communicates with other services mostly using model classes.<br/>
 *
 * Whenever possible, its public abstract API should not be overriden, but its protected methods instead.
 *
 * @param <M> The models class
 * @param <E> The entity class
 */
@Transactional
public abstract class CrudGenericService<M, E>
  implements CrudService<M, E> {
  protected static final String ITEM_NOT_FOUND = "Requested item(s) not found";
  protected static final String ITEM_ALREADY_EXISTS = "The item already exists";
  private final Repository<E> repository;
  private final ConverterService<M, E> converter;
  private final PatchService<M, E> patchService;

  protected CrudGenericService(
    Repository<E> repository,
    ConverterService<M, E> converter,
    PatchService<M, E> patchService
  ) {
    this.repository = repository;
    this.converter = converter;
    this.patchService = patchService;
  }

  /**
   * Converts a model class instance to an entity instance, saves it and returns it back as a model copy of the persisted entity.
   *
   * @param input model Pojo instance to be converted and inserted.
   * @throws BadInputException     When the data required from the input object is invalid or insufficient to build a proper entity.
   * @throws EntityExistsException When the data is a duplicate of an existing entity.
   */
  @Override
  public M create(M input)
    throws BadInputException, EntityExistsException {
    this.validateInputPojoBeforeCreation(input);
    E preparedEntity = this.prepareNewEntityFromInputPojo(input);
    return this.persist(preparedEntity);
  }

  /**
   * Read data from repository, convert each entity to its equivalent model class and
   * return the collected data in a {@link org.trebol.api.models.DataPagePojo}.
   */
  @Override
  public DataPagePojo<M> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters) {
    Pageable pagination = ((order == null) ?
      PageRequest.of(pageIndex, pageSize) :
      PageRequest.of(pageIndex, pageSize, order));
    long totalCount = ((filters == null) ?
      repository.count() :
      repository.count(filters));
    Page<E> iterable = ((filters == null) ?
      repository.findAll(pagination) :
      repository.findAll(filters, pagination));
    List<M> pojoList = new ArrayList<>();
    for (E item : iterable) {
      M outputItem = converter.convertToPojo(item);
      pojoList.add(outputItem);
    }
    return new DataPagePojo<>(pojoList, pageIndex, totalCount, pageSize);
  }

  /**
   * @throws EntityNotFoundException When no entity matches the given example.
   * @throws BadInputException       When the data in the input object is not valid.
   */
  @Override
  public M update(M input)
    throws EntityNotFoundException, BadInputException {
    Optional<E> match = this.getExisting(input);
    if (match.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    return this.persistEntityWithUpdatesFromPojo(input, match.get());
  }

  /**
   * @throws EntityNotFoundException When no entity matches the given filtering conditions.
   * @throws BadInputException       When the data in the input object is not valid.
   */
  @Override
  public M update(M input, Predicate filters)
    throws EntityNotFoundException, BadInputException {
    Optional<E> firstMatch = repository.findOne(filters);
    if (firstMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    return this.persistEntityWithUpdatesFromPojo(input, firstMatch.get());
  }

  /**
   * @throws EntityNotFoundException When no entity matches the given filtering conditions.
   */
  @Override
  public void delete(Predicate filters)
    throws EntityNotFoundException {
    long count = repository.count(filters);
    if (count == 0) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    repository.deleteAll(repository.findAll(filters));
  }

  /**
   * @throws EntityNotFoundException When no entity matches the given filtering conditions.
   */
  @Override
  public M readOne(Predicate filters)
    throws EntityNotFoundException {
    Optional<E> entity = repository.findOne(filters);
    if (entity.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    E found = entity.get();
    return converter.convertToPojo(found);
  }

  protected final M persist(E preparedEntity) {
    E result = repository.saveAndFlush(preparedEntity);
    return converter.convertToPojo(result);
  }

  /**
   * Copies changes from a models to an entity.
   * Executes right before updating (persisting) data.
   * Ideal overridable method to include cascading entity relationships.
   *
   * @param changes        A Pojo class instance with the data that is being submitted
   * @param existingEntity An existing entity class instance that will be updated
   * @return The resulting Pojo class instance
   * @throws BadInputException If data in Pojo is insufficient, incorrect, malformed, etc
   */
  protected M persistEntityWithUpdatesFromPojo(M changes, E existingEntity)
    throws BadInputException {
    E updatedEntity = patchService.patchExistingEntity(changes, existingEntity);
    if (existingEntity.equals(updatedEntity)) {
      return changes;
    }
    return this.persist(updatedEntity);
  }

  /**
   * Generic validation routine. Should be be called at the beginning of the create() method.
   *
   * @param inputPojo A model to validate
   * @throws BadInputException If the model does not have a valid identifying property
   * @throws BadInputException If the model does not have a valid identifying property
   */
  protected void validateInputPojoBeforeCreation(M inputPojo) throws BadInputException {
    if (this.getExisting(inputPojo).isPresent()) {
      throw new EntityExistsException(ITEM_ALREADY_EXISTS);
    }
  }

  /**
   * Creates a new entity from a model classs.
   * Executes right before persisting data.
   * Ideal overridable method to include cascading entity relationships.
   *
   * @param inputPojo A models to convert to an entity
   * @return An entity object equivalent to the provided models, ready for saving
   * @throws BadInputException If the models does not have a valid identifying property
   */
  protected E prepareNewEntityFromInputPojo(M inputPojo) throws BadInputException {
    return converter.convertToNewEntity(inputPojo);
  }
}
