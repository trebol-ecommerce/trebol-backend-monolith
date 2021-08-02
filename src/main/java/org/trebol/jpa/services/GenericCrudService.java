package org.trebol.jpa.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.trebol.api.DataPage;

import com.querydsl.core.types.Predicate;

import org.trebol.jpa.GenericEntity;
import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.exceptions.EntityAlreadyExistsException;

/**
 * Abstract service that sends and receives data with pojos and keep entities
 * out of public scope. API controllers should wire to subclasses of this.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The pojo class
 * @param <E> The entity class
 * @param <I> The identifier class
 */
public abstract class GenericCrudService<P, E extends GenericEntity<I>, I>
    implements CrudService<P, I>, TwoWayEntityPojoConverterService<E, P> {

  protected GenericRepository<E, I> repository;

  public GenericCrudService(GenericRepository<E, I> repository) {
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
  @Nullable
  @Override
  public I create(P inputPojo) throws EntityAlreadyExistsException {
    E input = pojo2Entity(inputPojo);
    try {
      E output = repository.saveAndFlush(input);
      return output.getId();
    } catch (DataIntegrityViolationException exc) {
      throw new EntityAlreadyExistsException("The provided data conflicts with existing data", exc);
    }
  }

  /**
   * Read entities, convert them to pojos and return the collection.
   */
  @Override
  public DataPage<P> read(int pageSize, int pageIndex, Predicate filters) {
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);
    Page<E> iterable = getAllEntities(paged, filters);
    int totalCount = Long.valueOf(repository.count(filters)).intValue();

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
   * to pojo and return. If it does not differ, return as-is.
   */
  @Nullable
  @Override
  public I update(P input, I id) {
    Optional<E> existing = repository.findById(id);
    if (!existing.isPresent()) {
      return null;
    } else {
      E existingEntity = existing.get();
      E newEntity = pojo2Entity(input);
      if (newEntity.equals(existingEntity)) {
        return id;
      } else {
        try {
          E result = repository.saveAndFlush(newEntity);
          return result.getId();
        } catch (Exception exc) {
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(I id) {
    try {
      repository.deleteById(id);
      repository.flush();
      return !repository.existsById(id);
    } catch (Exception exc) {
      return false;
    }
  }

  @Nullable
  @Override
  public P find(I id) {
    Optional<E> entityById = repository.findById(id);
    if (!entityById.isPresent()) {
      return null;
    } else {
      E found = entityById.get();
      P foundPojo = entity2Pojo(found);
      return foundPojo;
    }
  }

  @Nullable
  @Override
  public P find(Predicate filters) {
    Optional<E> entity = repository.findOne(filters);
    if (!entity.isPresent()) {
      return null;
    } else {
      E found = entity.get();
      P foundPojo = entity2Pojo(found);
      return foundPojo;
    }
  }
}
