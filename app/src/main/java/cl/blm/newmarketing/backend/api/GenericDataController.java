package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for API controllers that handle CRUD requests.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * 
 * @param <P> The Entity class
 * @param <E> The Identifier class
 */
public interface GenericDataController<E, I> {

  E create(E input);

  E readOne(I id);

  Collection<E> readMany(Map<String, String> requestParams);

  Collection<E> readMany(Integer pageSize, Map<String, String> requestParams);

  Collection<E> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);

  E update(E input);

  E update(E input, Integer id);

  boolean delete(Integer id);
}
