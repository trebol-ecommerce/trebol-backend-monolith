package cl.blm.newmarketing.backend.rest.controllers;

import java.util.Collection;
import java.util.Map;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.BackendAppGlobals;
import cl.blm.newmarketing.backend.rest.services.CrudService;

public abstract class EntityCrudController<E, K> {

  private BackendAppGlobals globals;
  protected CrudService<E, K> crudService;

  public EntityCrudController(BackendAppGlobals globals, CrudService<E, K> crudService) {
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
