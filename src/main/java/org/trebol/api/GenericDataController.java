package org.trebol.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.querydsl.core.types.Predicate;

import org.trebol.config.CustomProperties;
import org.trebol.jpa.GenericJpaCrudService;

/**
 * Abstraction for CrudControllers that communicate with a GenericJpaCrudService.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public abstract class GenericDataController<P, E>
  implements IDataController<P> {

  protected CustomProperties customProperties;
  protected GenericJpaCrudService<P, E> crudService;

  public GenericDataController(CustomProperties customProperties, GenericJpaCrudService<P, E> crudService) {
    this.customProperties = customProperties;
    this.crudService = crudService;
  }

  /**
   * Retrieve a page of items with a fixed size and offset index.
   * An optional Map (like query string parameters) can be provided for filtering criteria.
   *
   * @param requestPageSize Item count in page. If left null, its value will be overriden.
   * @param requestPageIndex Page offset, 0-based. If left null, its value will be overriden.
   * @param requestParams May contain filtering conditions and/or page size & page index parameters.
   * @see Predicate
   * @return
   */
  @Override
  public DataPage<P> readMany(Integer requestPageSize, Integer requestPageIndex, @NotNull Map<String, String> requestParams) {

    int pageSize = this.determineRequestedPageSize(requestPageSize, requestParams);
    int pageIndex = this.determineRequestedPageIndex(requestPageIndex, requestParams);

    Predicate filters = null;
    if (!requestParams.isEmpty()) {
      filters = crudService.parsePredicate(requestParams);
    }

    return crudService.readMany(pageSize, pageIndex, filters);
  }

  public Map<String, String> handleException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  private int determineRequestedPageIndex(Integer requestPageIndex, Map<String, String> allRequestParams) throws NumberFormatException {
    int pageIndex = 0;
    if (requestPageIndex != null && requestPageIndex > 0) {
      pageIndex = requestPageIndex - 1;
    } else if (allRequestParams.containsKey("pageIndex")) {
      pageIndex = Integer.valueOf(allRequestParams.get("pageIndex"));
    }
    return pageIndex;
  }

  private int determineRequestedPageSize(Integer requestPageSize, Map<String, String> allRequestParams) throws NumberFormatException {
    int pageSize = customProperties.getItemsPerPage();
    if (requestPageSize != null && requestPageSize > 0) {
      pageSize = requestPageSize;
    } else if (allRequestParams.containsKey("pageSize")) {
      pageSize = Integer.valueOf(allRequestParams.get("pageSize"));
    }
    return pageSize;
  }
}
