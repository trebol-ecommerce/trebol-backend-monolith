package org.trebol.api;

import java.util.Map;

/**
 * Interface for API controllers that handle CRUD requests.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <P> The Pojo class
 * @param <I> The Identifier class
 */
public interface CrudController<P, I> {

  void create(P input);

  P readOne(I id);

  DataPage<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);

  void update(P input, Integer id);

  void delete(Integer id);
}
