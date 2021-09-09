package org.trebol.jpa;

import org.trebol.exceptions.BadInputException;

/**
 * Interface to ensure type-safety for services that require converting from
 * Entities to Pojos and viceversa.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public interface IJpaConverterService<P, E> {

  /**
   * Straightly converts a Pojo to a new entity, assuming that the Pojo is already @Valid.
   * @param source The source Pojo.
   * @return A new entity, prepared to be saved to the database.
   * @throws BadInputException
   */
  E convertToNewEntity(P source) throws BadInputException;

  /**
   * Accurately updates entity with new data property-by-property.
   * @param source The Pojo containing data updates.
   * @param target The target entity.
   * @throws BadInputException
   */
  void applyChangesToExistingEntity(P source, E target) throws BadInputException;

  /**
   * Converts an existing Entity to its Pojo equivalent.
   * @param source The source entity.
   * @return The resulting Pojo.
   */
  P convertToPojo(E source);
}
