/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.IJpaRepository;
import org.trebol.pojo.DataPagePojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple CRUD service abstraction. Communicates with other services only through Pojos, keeping JPA entity classes only within their own scope.
 * Whenever possible, its public abstract API should not be overriden, but its protected methods instead.
 *
 * @param <P> The pojo class
 * @param <E> The entity class
 */
@Transactional
public abstract class GenericCrudService<P, E>
  implements ICrudService<P, E> {
  private final IJpaRepository<E> repository;

  protected static final String ITEM_NOT_FOUND = "Requested item(s) not found";
  protected static final String ITEM_ALREADY_EXISTS = "The item already exists";
  private final ITwoWayConverterService<P, E> converter;
  private final IDataTransportService<P, E> dataTransportService;

  protected GenericCrudService(
    IJpaRepository<E> repository,
    ITwoWayConverterService<P, E> converter,
    IDataTransportService<P, E> dataTransportService
  ) {
    this.repository = repository;
    this.converter = converter;
    this.dataTransportService = dataTransportService;
  }

  /**
   * Converts a pojo to an entity, saves it and returns it back as a brand new pojo equivalent.
   *
   * @param inputPojo The Pojo instance to be converted and inserted.
   */
  @Override
  public P create(P inputPojo)
    throws BadInputException, EntityExistsException {
    this.validateInputPojoBeforeCreation(inputPojo);
    E preparedEntity = this.prepareNewEntityFromInputPojo(inputPojo);
    return this.persist(preparedEntity);
  }

  /**
   * Read entities, convert them to pojos and return the collection.
   */
  @Override
  public DataPagePojo<P> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters) {
    Pageable pagination = ((order == null) ?
      PageRequest.of(pageIndex, pageSize) :
      PageRequest.of(pageIndex, pageSize, order));
    long totalCount = ((filters == null) ?
      repository.count() :
      repository.count(filters));
    Page<E> iterable = ((filters == null) ?
      repository.findAll(pagination) :
      repository.findAll(filters, pagination));
    List<P> pojoList = new ArrayList<>();
    for (E item : iterable) {
      P outputItem = converter.convertToPojo(item);
      pojoList.add(outputItem);
    }
    return new DataPagePojo<>(pojoList, pageIndex, totalCount, pageSize);
  }

  @Override
  public P update(P input)
    throws EntityNotFoundException, BadInputException {
    Optional<E> match = this.getExisting(input);
    if (match.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    return this.persistEntityWithUpdatesFromPojo(input, match.get());
  }

  @Override
  public P update(P input, Predicate filters)
    throws EntityNotFoundException, BadInputException {
    Optional<E> firstMatch = repository.findOne(filters);
    if (firstMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    return this.persistEntityWithUpdatesFromPojo(input, firstMatch.get());
  }

  @Override
  public void delete(Predicate filters)
    throws EntityNotFoundException {
    long count = repository.count(filters);
    if (count == 0) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    repository.deleteAll(repository.findAll(filters));
  }

  @Override
  public P readOne(Predicate filters)
    throws EntityNotFoundException {
    Optional<E> entity = repository.findOne(filters);
    if (entity.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    E found = entity.get();
    return converter.convertToPojo(found);
  }

  protected final P persist(E preparedEntity) {
    E result = repository.saveAndFlush(preparedEntity);
    return converter.convertToPojo(result);
  }

  /**
   * Copies changes from a pojo to an entity.
   * Executes right before updating (persisting) data.
   * Ideal overridable method to include cascading entity relationships.
   *
   * @param changes        A Pojo class instance with the data that is being submitted
   * @param existingEntity An existing entity class instance that will be updated
   * @return The resulting Pojo class instance
   * @throws BadInputException If data in Pojo is insufficient, incorrect, malformed, etc
   */
  protected P persistEntityWithUpdatesFromPojo(P changes, E existingEntity)
    throws BadInputException {
    E updatedEntity = dataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    if (existingEntity.equals(updatedEntity)) {
      return changes;
    }
    return this.persist(updatedEntity);
  }

  /**
   * Base entity-specific validation  method.
   * Should be called at the beginning of the create() method.
   *
   * @param inputPojo A pojo to validate
   * @throws BadInputException If the pojo does not have a valid identifying property
   */
  protected void validateInputPojoBeforeCreation(P inputPojo) throws BadInputException {
    if (this.getExisting(inputPojo).isPresent()) {
      throw new EntityExistsException(ITEM_ALREADY_EXISTS);
    }
  }

  /**
   * Creates a new entity from a pojo.
   * Executes right before persisting data.
   * Ideal overridable method to include cascading entity relationships.
   *
   * @param inputPojo A pojo to convert to an entity
   * @return An entity object equivalent to the provided pojo, ready for saving
   * @throws BadInputException If the pojo does not have a valid identifying property
   */
  protected E prepareNewEntityFromInputPojo(P inputPojo) throws BadInputException {
    return converter.convertToNewEntity(inputPojo);
  }
}
