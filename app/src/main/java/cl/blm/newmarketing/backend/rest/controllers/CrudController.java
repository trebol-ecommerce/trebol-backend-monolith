package cl.blm.newmarketing.backend.rest.controllers;

import java.util.Collection;
import java.util.Map;

public interface CrudController<P, I> {

  P create(P input);

  P readOne(I id);

  Collection<P> readMany(Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Map<String, String> requestParams);

  Collection<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);

  P update(P input);

  P update(P input, Integer id);

  boolean delete(Integer id);
}
