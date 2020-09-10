package cl.blm.newmarketing.backend.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.CustomProperties;
import cl.blm.newmarketing.backend.jpa.GenericEntity;
import cl.blm.newmarketing.backend.services.data.GenericEntityDataService;

/**
 * Abstraction for CrudControllers that communicate with a
 * GenericEntityDataService.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public abstract class GenericEntityDataController<P, E extends GenericEntity<I>, I>
    implements CrudController<P, I> {
  protected static Logger LOG;
  protected CustomProperties globals;
  protected GenericEntityDataService<P, E, I> dataService;

  public GenericEntityDataController(Logger logger, CustomProperties globals,
      GenericEntityDataService<P, E, I> dataService) {
    LOG = logger;
    this.globals = globals;
    this.dataService = dataService;
  }

  /**
   * Pass a new item to the service.
   * 
   * @return The resulting item's ID
   */
  public I create(P input) {
    LOG.info("create");
    I resultId = dataService.create(input);
    return resultId;
  }

  /**
   * Fetch one item by a provided id.
   *
   * @param id The identifier of the item
   * @return The item
   */
  public P readOne(I id) {
    LOG.info("read");
    P found = dataService.find(id);
    return found;
  }

  /**
   * Retrieve a page of items of with a fixed size and initial index. An optional
   * Map (like query string parameters) can be provided for filtering criteria.
   *
   * @param requestPageSize
   * @param requestPageIndex
   * @param allRequestParams
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

  /**
   * Update the item found with an id, using data provided as input.
   * 
   * @param input
   * @param id
   * @return
   */
  public I update(P input, I id) {
    LOG.info("update");
    I resultId = dataService.update(input, id);
    return resultId;
  }

  /**
   * Delete an item by its id.
   * 
   * @param id
   * @return
   */
  public boolean delete(I id) {
    LOG.info("delete");
    boolean result = dataService.delete(id);
    return result;
  }

  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    LOG.warn("MethodArgumentNotValidException handled: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
