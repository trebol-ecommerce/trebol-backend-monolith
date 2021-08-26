package org.trebol.jpa.services;

import javax.annotation.Nullable;

/**
 * Interface to ensure type-safety for services that require converting from
 * Entities to Pojos and viceversa.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public interface TwoWayPojoEntityConverterService<P, E> {

  @Nullable
  E pojo2Entity(P source);

  @Nullable
  P entity2Pojo(E source);
}
