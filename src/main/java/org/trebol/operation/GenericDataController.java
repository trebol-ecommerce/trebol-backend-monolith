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
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public abstract class GenericDataController<P, E>
  extends GenericPaginationController
  implements IDataController<P> {

  protected final ICrudJpaService<P, Long> crudService;
  protected final IPredicateJpaService<E> predicateService;

  public GenericDataController(OperationProperties operationProperties,
                               ICrudJpaService<P, Long> crudService,
                               IPredicateJpaService<E> predicateService) {
    super(operationProperties);
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
}
