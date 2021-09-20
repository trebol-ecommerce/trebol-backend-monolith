package org.trebol.operation;

import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;

import javassist.NotFoundException;

/**
 * Interface for API controllers that handle CRUD requests involving unique identifiers.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 * @param <I> The Identifier class
 */
public interface IDataCrudController<P, I>
  extends IDataController<P> {

  void create(P input) throws BadInputException, EntityAlreadyExistsException;

  P readOne(I id) throws NotFoundException;

  void update(P input, I id) throws BadInputException, NotFoundException;

  void delete(I id) throws NotFoundException;
}
