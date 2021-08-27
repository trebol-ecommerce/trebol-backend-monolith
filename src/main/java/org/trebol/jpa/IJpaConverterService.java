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

  E pojo2Entity(P source) throws BadInputException;

  P entity2Pojo(E source);
}
