package org.trebol.operation;

import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.trebol.config.OperationProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

public class GenericDataCrudController<P, E>
  extends GenericDataController<P, E>
  implements IDataCrudController<P> {

  protected GenericDataCrudController(OperationProperties operationProperties,
                                      GenericCrudJpaService<P, E> crudService,
                                      IPredicateJpaService<E> predicateService) {
    super(operationProperties, crudService, predicateService);
  }

  @Override
  public void create(P input) throws BadInputException, EntityAlreadyExistsException {
    crudService.create(input);
  }

  @Override
  public void update(P input, Map<String, String> requestParams) throws BadInputException, NotFoundException {
    if (!requestParams.isEmpty()) {
      Predicate predicate = predicateService.parseMap(requestParams);
      crudService.update(input, predicate);
    } else {
      crudService.update(input);
    }
  }

  @Override
  public void delete(Map<String, String> requestParams) throws NotFoundException {
    Predicate predicate = predicateService.parseMap(requestParams);
    crudService.delete(predicate);
  }
}
