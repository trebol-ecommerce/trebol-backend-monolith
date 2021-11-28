package org.trebol.operation;

import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;

import javassist.NotFoundException;

import java.util.Map;

/**
 * Interface for API controllers that handle CRUD requests involving unique identifiers.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 */
public interface IDataCrudController<P>
  extends IDataController<P> {

  void create(P input) throws BadInputException, EntityAlreadyExistsException;

  void update(P input, Map<String, String> requestParams) throws BadInputException, NotFoundException;

  void delete(Map<String, String> requestParams) throws NotFoundException;
}
