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
 * Base abstraction for JPA-based CRUD services that communicate with Pojos, keeping entity classes out of scope
 * @param <P> The pojo class
 * @param <E> The entity class
 */
@Transactional
public abstract class GenericCrudJpaService<P, E>
  implements ICrudJpaService<P> {

  // avoid shadowing this field; as implementations should always refer to their specific repositories
  private final IJpaRepository<E> repository;

  protected static final String ITEM_NOT_FOUND = "Requested item(s) not found";
  protected static final String ITEM_ALREADY_EXISTS = "The item already exists";
  protected final ITwoWayConverterJpaService<P, E> converter;
  protected final IDataTransportJpaService<P, E> dataTransportService;

  protected GenericCrudJpaService(IJpaRepository<E> repository,
                               ITwoWayConverterJpaService<P, E> converter,
                               IDataTransportJpaService<P, E> dataTransportService) {
    this.repository = repository;
    this.converter = converter;
    this.dataTransportService = dataTransportService;
  }

  /**
   * Attempts to match the given pojo class instance to an existing entity in the persistence context.
   * @param example The pojo class instance that should hold a valid identifying property
   * @return A possible entity match
   * @throws BadInputException When the pojo doesn't have its identifying property.
   */
  public abstract Optional<E> getExisting(P example) throws BadInputException;

  /**
   * Convert a pojo to an entity, save it, convert it back to a pojo and return
   * it.
   * @param inputPojo The Pojo instance to be converted and inserted.
   */
  @Override
  public P create(P inputPojo)
      throws BadInputException, EntityExistsException {
    if (this.getExisting(inputPojo).isPresent()) {
      throw new EntityExistsException(ITEM_ALREADY_EXISTS);
    }
    E input = converter.convertToNewEntity(inputPojo);
    E output = repository.saveAndFlush(input);
    return converter.convertToPojo(output);
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
    return this.doUpdate(input, match.get());
  }

  @Override
  public P update(P input, Predicate filters)
      throws EntityNotFoundException, BadInputException {
    Optional<E> firstMatch = repository.findOne(filters);
    if (firstMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }
    return this.doUpdate(input, firstMatch.get());
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

  /**
   * Applies changes, and flushes. If no changes are detected, return changes as-is
   * @param changes A Pojo class instance with the data that is being submitted
   * @param existingEntity An existing entity class instance that will be updated
   * @return The resulting Pojo class instance
   * @throws BadInputException If data in Pojo is insufficient, incorrect, malformed, etc
   */
  protected P doUpdate(P changes, E existingEntity)
      throws BadInputException {
    E updatedEntity = dataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    if (existingEntity.equals(updatedEntity)) {
      return changes;
    }
    E output = repository.saveAndFlush(updatedEntity);
    return converter.convertToPojo(output);
  }
}
