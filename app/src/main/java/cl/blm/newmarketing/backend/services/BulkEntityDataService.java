package cl.blm.newmarketing.backend.services;

import java.util.Collection;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.jpa.GenericEntity;

/**
 * A specialized extension of CrudService that supports bulk (massive) CRUD
 * operations.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * 
 * @param <T> The type class of the item.
 * @param <I> The identifier type class of the item.
 */
public interface BulkEntityDataService<T extends GenericEntity<I>, I>
    extends EntityDataService<T, I> {

  /**
   * Inserts many items.
   *
   * @param dto The collection of items to save.
   *
   * @return A collection of resulting items. Some may be null if they weren't
   *         successfully created.
   */
  public Collection<T> bulkCreate(Collection<T> dto);

  /**
   * Finds and deletes all items that match the given conditions.
   *
   * @param filters A single condition or a set of conditions.
   *
   * @return The amount of items affected.
   */
  public long bulkDelete(Predicate filters);
}
