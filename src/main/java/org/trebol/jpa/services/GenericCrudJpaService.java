package org.trebol.jpa.services;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.IJpaRepository;
import org.trebol.pojo.DataPagePojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base abstraction for JPA-based CRUD services that communicate with Pojos, keeping entity classes out of scope.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The pojo class
 * @param <E> The entity class
 */
@Transactional
public abstract class GenericCrudJpaService<P, E>
  implements ICrudJpaService<P, Long> {

  // avoid shadowing this field; as implementations should always refer to their specific repositories
  private final IJpaRepository<E> repository;

  protected static final String ITEM_NOT_FOUND = "Requested item(s) not found";
  protected static final String ITEM_ALREADY_EXISTS = "The item already exists";
  protected final ITwoWayConverterJpaService<P, E> converter;
  protected final Logger logger;

  public GenericCrudJpaService(IJpaRepository<E> repository,
                               ITwoWayConverterJpaService<P, E> converter,
                               Logger logger) {
    this.repository = repository;
    this.converter = converter;
    this.logger = logger;
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
  public P create(P inputPojo) throws BadInputException, EntityAlreadyExistsException {
    if (this.getExisting(inputPojo).isPresent()) {
      throw new EntityAlreadyExistsException(ITEM_ALREADY_EXISTS);
    } else {
      E input = converter.convertToNewEntity(inputPojo);
      E output = repository.saveAndFlush(input);
      return converter.convertToPojo(output);
    }
  }

  /**
   * Read entities, convert them to pojos and return the collection.
   */
  @Override
  public DataPagePojo<P> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters) {
    // TODO figure out sort order parameter
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
  public P update(P input) throws NotFoundException, BadInputException {
    Optional<E> match = this.getExisting(input);
    if (match.isEmpty()) {
      throw new NotFoundException(ITEM_NOT_FOUND);
    } else {
      return this.doUpdate(input, match.get());
    }
  }

  @Override
  public P update(P input, Predicate filters) throws NotFoundException, BadInputException {
    Optional<E> firstMatch = repository.findOne(filters);
    if (firstMatch.isEmpty()) {
      throw new NotFoundException(ITEM_NOT_FOUND);
    } else {
      return this.doUpdate(input, firstMatch.get());
    }
  }

  @Override
  public void delete(Predicate filters) throws NotFoundException {
    long count = repository.count(filters);
    if (count == 0) {
      throw new NotFoundException(ITEM_NOT_FOUND);
    } else {
      repository.deleteAll(repository.findAll(filters));
    }
  }

  @Override
  public P readOne(Predicate filters) throws NotFoundException {
    Optional<E> entity = repository.findOne(filters);
    if (entity.isEmpty()) {
      throw new NotFoundException(ITEM_NOT_FOUND);
    } else {
      E found = entity.get();
      return converter.convertToPojo(found);
    }
  }

  /**
   * Applies changes, and flushes. If no changes are detected, return input as-is
   * @param input A Pojo class instance with the data that is being submitted
   * @param existingEntity An existing entity class instance that will be updated
   * @return The resulting Pojo class instance
   * @throws BadInputException If data in Pojo is insufficient, incorrect, malformed, etc
   */
  protected P doUpdate(P input, E existingEntity) throws BadInputException {
    E updatedEntity = converter.applyChangesToExistingEntity(input, existingEntity);
    if (existingEntity.equals(updatedEntity)) {
      return input;
    } else {
      E output = repository.saveAndFlush(updatedEntity);
      return converter.convertToPojo(output);
    }
  }
}
