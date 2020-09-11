package cl.blm.newmarketing.backend.services.data;

/**
 * Interface to ensure type-safety for services that require converting from
 * Entities to Pojos and viceversa.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <E> The Entity class
 * @param <P> The Pojo class
 */
public interface TwoWayEntityPojoConverter<E, P> {
  P entity2Pojo(E source);

  E pojo2Entity(P source);
}
