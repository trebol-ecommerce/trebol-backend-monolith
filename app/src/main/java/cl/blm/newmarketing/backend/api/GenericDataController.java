package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for API controllers that handle CRUD requests.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * 
 * @param <P> The Pojo class
 * @param <P> The Identifier class
 */
public interface GenericDataController<P, I> {

  I create(P input);

  P readOne(I id);

  Collection<P> readMany(Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);

  I update(P input);

  I update(P input, Integer id);

  boolean delete(Integer id);
}
