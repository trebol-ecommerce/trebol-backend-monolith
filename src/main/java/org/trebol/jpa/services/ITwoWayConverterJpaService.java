package org.trebol.jpa.services;

import org.trebol.exceptions.BadInputException;

import javax.annotation.Nullable;

/**
 * Type-safe interface for direct interaction between Entities and Pojos
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public interface ITwoWayConverterJpaService<P, E> {

  /**
   * Straightly converts a Pojo to a new entity, assuming that the Pojo is already @Valid.
   * This method DOES NOT persist said entity.
   * @param source The source Pojo.
   * @return A new entity, prepared to be saved to the database.
   * @throws BadInputException If the source object does not include required data or has invalid values
   */
  E convertToNewEntity(P source) throws BadInputException;

  /**
   * Creates a clone entity, then updates it with new data from a Pojo class, putting differences
   * property-by-property. This method DOES NOT persist the aforementioned entity.
   * @param source The Pojo containing data updates.
   * @param target The target entity.
   * @throws BadInputException If the object with changes has invalid values
   */
  E applyChangesToExistingEntity(P source, E target) throws BadInputException;

  /**
   * Converts an existing Entity to its Pojo equivalent.
   * @param source The source entity.
   * @return The resulting Pojo.
   */
  @Nullable
  P convertToPojo(E source);
}
