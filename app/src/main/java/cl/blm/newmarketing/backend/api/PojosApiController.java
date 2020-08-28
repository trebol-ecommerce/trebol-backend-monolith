package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for controllers that handle CRUD API requests .
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public interface PojosApiController<P, I> {

  P create(P input);

  P readOne(I id);

  Collection<P> readMany(Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);

  P update(P input);

  P update(P input, Integer id);

  boolean delete(Integer id);
}
