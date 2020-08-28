package cl.blm.newmarketing.backend.services.impl;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.GenericRepository;
import cl.blm.newmarketing.backend.services.DtoCrudService;

public abstract class GenericCrudService<E, I>
    implements DtoCrudService<E, I> {
  protected static Logger LOG;

  private GenericRepository<E, I> repository;

  public GenericCrudService(Logger logger, GenericRepository<E, I> repository) {
    LOG = logger;
    this.repository = repository;
  }

  @Nullable
  @Override
  public E create(E newClient) {
    LOG.debug("create({})", newClient);
    E result = repository.saveAndFlush(newClient);
    return result;
  }

  @Override
  public Collection<E> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);

    Page<E> iterable;
    if (filters == null) {
      iterable = repository.findAll(paged);
    } else {
      iterable = repository.findAll(filters, paged);
    }

    return iterable.getContent();
  }

  @Nullable
  @Override
  public E update(E input, I id) {
    LOG.debug("update({})", input);
    Optional<E> existing = repository.findById(id);
    if (!existing.isPresent()) {
      return null;
    } else {
      E existingPerson = existing.get();
      if (input.equals(existingPerson)) {
        return input;
      } else {
        try {
          E result = repository.saveAndFlush(input);
          return result;
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
  public E find(I id) {
    LOG.debug("find({})", id);
    Optional<E> personById = repository.findById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      E found = personById.get();
      return found;
    }
  }
}
