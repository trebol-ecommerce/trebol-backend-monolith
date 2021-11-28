package org.trebol.operation;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaService;
import org.trebol.pojo.ImagePojo;

import java.util.Map;

public abstract class GenericDataCrudController<P, E>
  extends GenericDataController<P, E>
  implements IDataCrudController<P> {

  protected GenericDataCrudController(OperationProperties operationProperties,
                                      GenericJpaService<P, E> crudService) {
    super(operationProperties, crudService);
  }

  @Override
  public void create(P input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  public void update(P input, Map<String, String> requestParams) throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = crudService.parsePredicate(requestParams);
      crudService.update(input, predicate);
    } else {
      crudService.update(input);
    }
  }

  @Override
  public void delete(Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = crudService.parsePredicate(requestParams);
    crudService.delete(predicate);
  }
}
