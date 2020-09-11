package cl.blm.newmarketing.store.services.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.jpa.GenericEntity;
import cl.blm.newmarketing.store.jpa.GenericRepository;
import cl.blm.newmarketing.store.services.EntityCrudService;
import cl.blm.newmarketing.store.services.TwoWayEntityPojoConverterService;

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
public abstract class GenericEntityCrudService<P, E extends GenericEntity<I>, I>
    implements EntityCrudService<P, I>, TwoWayEntityPojoConverterService<E, P> {
  protected static Logger LOG;

  protected GenericRepository<E, I> repository;

  public GenericEntityCrudService(Logger logger, GenericRepository<E, I> repository) {
    LOG = logger;
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
   */
  @Nullable
  @Override
  public I create(P inputPojo) {
    LOG.debug("create({})", inputPojo);
    E input = pojo2Entity(inputPojo);
    E output = repository.saveAndFlush(input);
    return output.getId();
  }

  /**
   * Read entities, convert them to pojos and return the collection.
   */
  @Override
  public Collection<P> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);
    Page<E> iterable = getAllEntities(paged, filters);

    List<P> outputPojoList = new ArrayList<>();
    for (E item : iterable) {
      P outputItem = entity2Pojo(item);
      outputPojoList.add(outputItem);
    }

    return outputPojoList;
  }

  /**
   * Look up the entity, save it if exists and differs from existing, convert back
   * to pojo and return. If it does not differ, return as-is.
   */
  @Nullable
  @Override
  public I update(P input, I id) {
    LOG.debug("update({})", input);
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
          LOG.error("Person could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(I id) {
    LOG.debug("delete({})", id);
    try {
      repository.deleteById(id);
      repository.flush();
      return !repository.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete person with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public P find(I id) {
    LOG.debug("find({})", id);
    Optional<E> personById = repository.findById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      E found = personById.get();
      P foundPojo = entity2Pojo(found);
      return foundPojo;
    }
  }
}
