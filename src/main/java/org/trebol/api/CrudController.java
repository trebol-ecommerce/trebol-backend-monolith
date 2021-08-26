package org.trebol.api;

import org.trebol.jpa.exceptions.EntityAlreadyExistsException;

import javassist.NotFoundException;

/**
 * Interface for API controllers that handle CRUD requests involving unique identifiers.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 * @param <I> The Identifier class
 */
public interface CrudController<P, I>
  extends DataController<P> {

  void create(P input) throws EntityAlreadyExistsException;

  P readOne(I id) throws NotFoundException;

  void update(P input, I id) throws NotFoundException;

  void delete(I id) throws NotFoundException;
}
