package org.trebol.operation;

import com.querydsl.core.types.Predicate;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.services.ICrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.pojo.DataPagePojo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * RestController that implements IDataController with a GenericJpaService.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public abstract class GenericDataController<P, E>
  implements IDataController<P> {

  protected final OperationProperties operationProperties;
  protected final ICrudJpaService<P, Long> crudService;
  protected final IPredicateJpaService<E> predicateService;

  public GenericDataController(OperationProperties operationProperties,
                               ICrudJpaService<P, Long> crudService,
                               IPredicateJpaService<E> predicateService) {
    this.operationProperties = operationProperties;
    this.crudService = crudService;
    this.predicateService = predicateService;
  }

  /**
   * Retrieve a page of items with a fixed size and offset index.
   * An optional Map (like query string parameters) can be provided for filtering criteria.
   *
   * @param requestPageSize Item count in page. If left null, its value will be overriden.
   * @param requestPageIndex Page offset, 0-based. If left null, its value will be overriden.
   * @param requestParams May contain filtering conditions and/or page size & page index parameters.
   * @see Predicate
   * @return A paged collection of Pojos.
   */
  @Override
  public DataPagePojo<P> readMany(Integer requestPageSize, Integer requestPageIndex, @NotNull Map<String, String> requestParams) {

    int pageSize = this.determineRequestedPageSize(requestPageSize, requestParams);
    int pageIndex = this.determineRequestedPageIndex(requestPageIndex, requestParams);

    Predicate filters = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      filters = predicateService.parseMap(requestParams);
    }

    return crudService.readMany(pageSize, pageIndex, filters);
  }

  private int determineRequestedPageIndex(Integer requestPageIndex, Map<String, String> allRequestParams)
      throws NumberFormatException {
    if (allRequestParams != null && allRequestParams.containsKey("pageIndex")) {
      return Integer.parseInt(allRequestParams.get("pageIndex"));
    } else if (requestPageIndex != null && requestPageIndex > 0) {
      return requestPageIndex;
    }
    return 0;
  }

  private int determineRequestedPageSize(Integer requestPageSize, Map<String, String> allRequestParams)
      throws NumberFormatException {
    if (allRequestParams != null && allRequestParams.containsKey("pageSize")) {
      return Integer.parseInt(allRequestParams.get("pageSize"));
    } else if (requestPageSize != null && requestPageSize > 0) {
      return requestPageSize;
    }
    return operationProperties.getItemsPerPage();
  }
}
