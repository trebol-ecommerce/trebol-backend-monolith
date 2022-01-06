package org.trebol.operation;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
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
   * @param requestParams May contain filtering conditions and/or page size & page index parameters.
   * @see Predicate
   * @return A paged collection of Pojos.
   */
  @Override
  public DataPagePojo<P> readMany(@NotNull Map<String, String> requestParams) {

    int pageIndex = this.determineRequestedPageIndex(requestParams);
    int pageSize = this.determineRequestedPageSize(requestParams);

    Sort order = null;
    if (requestParams != null && requestParams.containsKey("sortBy")) {
      order = this.determineSortOrder(requestParams);
    }

    Predicate filters = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      filters = predicateService.parseMap(requestParams);
    }

    return crudService.readMany(pageIndex, pageSize, order, filters);
  }

  /**
   * Handle simple sort order cases where the property resides directly in the target entity e.g. a product's barcode.
   * @param requestParams The query params map extracted from the request
   * @return A sort order
   */
  protected Sort determineSortOrder(Map<String, String> requestParams) {
    Sort sortBy = Sort.by(requestParams.get("sortBy"));
    switch (requestParams.get("order")) {
      case "asc": return sortBy.ascending();
      case "desc": return sortBy.descending();
      default: return sortBy;
    }
  }

  private int determineRequestedPageIndex(Map<String, String> allRequestParams)
      throws NumberFormatException {
    if (allRequestParams != null && allRequestParams.containsKey("pageIndex")) {
      return Integer.parseInt(allRequestParams.get("pageIndex"));
    }
    return 0;
  }

  private int determineRequestedPageSize(Map<String, String> allRequestParams)
      throws NumberFormatException {
    if (allRequestParams != null && allRequestParams.containsKey("pageSize")) {
      return Integer.parseInt(allRequestParams.get("pageSize"));
    }
    return operationProperties.getItemsPerPage();
  }
}
