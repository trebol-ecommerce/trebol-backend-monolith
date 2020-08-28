package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.Map;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 * A basic setup for a class that communicates with a CrudService interface
 * implementation (should be a @RestController).
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public abstract class DtoCrudServiceClient<E, K> {

  protected CustomProperties globals;
  protected DtoCrudService<E, K> crudService;

  public DtoCrudServiceClient(CustomProperties globals, DtoCrudService<E, K> crudService) {
    this.globals = globals;
    this.crudService = crudService;
  }

  /**
   * Retrieve a page of entities from the corresponding service. For filtering,
   * transforms a Map into a Predicate.
   *
   * @param requestPageSize
   * @param requestPageIndex
   * @param allRequestParams
   *
   * @see Predicate
   * @return
   */
  public Collection<E> readFromService(Integer requestPageSize, Integer requestPageIndex,
      Map<String, String> allRequestParams) {
    if (this.crudService == null) {
      throw new Error("CrudService is not implemented properly in calling controller");
    }
    int pageSize = globals.ITEMS_PER_PAGE;
    int pageIndex = 0;
    Predicate filters = null;

    if (requestPageSize != null && requestPageSize > 0) {
      pageSize = requestPageSize;
    }
    if (requestPageIndex != null && requestPageIndex > 0) {
      pageIndex = requestPageIndex - 1;
    }
    if (allRequestParams != null && !allRequestParams.isEmpty()) {
      filters = crudService.queryParamsMapToPredicate(allRequestParams);
    }

    return crudService.read(pageSize, pageIndex, filters);
  }
}
