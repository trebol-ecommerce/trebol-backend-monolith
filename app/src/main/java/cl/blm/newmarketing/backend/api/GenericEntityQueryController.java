package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.Map;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.model.GenericEntity;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 * Abstraction for controllers that communicate with a GenericDataService.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public abstract class GenericEntityQueryController<P, E extends GenericEntity<I>, I> {

  protected CustomProperties globals;
  protected GenericDataService<P, E, I> dataService;

  public GenericEntityQueryController(CustomProperties globals, GenericDataService<P, E, I> dataService) {
    this.globals = globals;
    this.dataService = dataService;
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
  public Collection<P> readMany(Integer requestPageSize, Integer requestPageIndex,
      Map<String, String> allRequestParams) {
    if (this.dataService == null) {
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
      filters = dataService.queryParamsMapToPredicate(allRequestParams);
    }

    return dataService.read(pageSize, pageIndex, filters);
  }
}
