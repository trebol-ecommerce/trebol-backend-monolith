package org.trebol.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.DataPage;
import org.trebol.exceptions.BadInputException;

import com.querydsl.core.types.Predicate;

import org.trebol.exceptions.EntityAlreadyExistsException;

import javassist.NotFoundException;

/**
 * Base abstraction for JPA-based CRUD services that communicate with POJOs, keeping JPA entities out of scope.
 *
 * API controllers should wire to subclasses of this.
 *
 * Because it extends Map2QueryDslPredicateConverterService, it's expected to comply to QueryDSL and
 * accept Predicate objects as filtering conditions.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The pojo class
 * @param <E> The entity class
 */
public abstract class GenericJpaCrudService<P, E>
  implements IJpaCrudService<P, Long, Predicate>, IJpaConverterService<P, E> {

  protected IJpaRepository<E> repository;

  public abstract boolean itemExists(P input) throws BadInputException;

  public GenericJpaCrudService(IJpaRepository<E> repository) {
    this.repository = repository;
  }

  /**
   * Query all entities for the type class. Override this method if you need
   * custom queries. Remember to declare the correct repository interface first.
   *
   * @param paged
   * @param filters
   * @return
   */
  public Page<E> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.findAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

  /**
   * Convert a pojo to an entity, save it, convert it back to a pojo and return
   * it.
   * @param inputPojo
   */
  @Transactional
  @Override
  public P create(P inputPojo) throws BadInputException, EntityAlreadyExistsException {
    if (this.itemExists(inputPojo)) {
      throw new EntityAlreadyExistsException("The item to be created already exists");
    } else {
      E input = this.pojo2Entity(inputPojo);
      E output = repository.saveAndFlush(input);
      P result = this.entity2Pojo(output);
      return result;
    }
  }

  /**
   * Read entities, convert them to pojos and return the collection.
   */
  @Override
  public DataPage<P> readMany(int pageSize, int pageIndex, Predicate filters) {
    // TODO figure out sort order parameter
    Pageable paged = PageRequest.of(pageIndex, pageSize);
    Page<E> iterable = this.getAllEntities(paged, filters);
    long totalCount = repository.count(filters);

    List<P> pojoList = new ArrayList<>();
    for (E item : iterable) {
      P outputItem = entity2Pojo(item);
      pojoList.add(outputItem);
    }

    DataPage<P> output = new DataPage(pojoList, pageIndex, totalCount, pageSize);

    return output;
  }

  /**
   * Look up the entity, save it if exists and differs from existing, convert back
   * to pojo and return.If it does not differ, return as-is.
   * @param input
   * @param id
   */
  @Override
  public P update(P input, Long id) throws NotFoundException, BadInputException {
    Optional<E> itemById = repository.findById(id);
    if (!itemById.isPresent()) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      E existingEntity = itemById.get();
      E newEntity = this.pojo2Entity(input);
      if (newEntity.equals(existingEntity)) {
        return input;
      } else {
        try {
          E output = repository.saveAndFlush(newEntity);
          P result = this.entity2Pojo(output);
          return result;
        } catch (Exception exc) {
          return null;
        }
      }
    }
  }

  @Override
  public void delete(Long id) throws NotFoundException {
    if (!repository.existsById(id)) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      repository.deleteById(id);
      repository.flush();
    }
  }

  @Override
  public P readOne(Long id) throws NotFoundException {
    Optional<E> entityById = repository.findById(id);
    if (!entityById.isPresent()) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      E found = entityById.get();
      P foundPojo = this.entity2Pojo(found);
      return foundPojo;
    }
  }

  @Override
  public P readOne(Predicate filters) throws NotFoundException {
    Optional<E> entity = repository.findOne(filters);
    if (!entity.isPresent()) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      E found = entity.get();
      P foundPojo = entity2Pojo(found);
      return foundPojo;
    }
  }
}
