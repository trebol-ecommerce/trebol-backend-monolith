package cl.blm.newmarketing.store.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.config.CustomProperties;
import cl.blm.newmarketing.store.jpa.GenericEntity;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

/**
 * Abstraction for CrudControllers that communicate with a
 * GenericEntityCrudService.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 * @param <E> The Entity class
 * @param <I> The Identifier class
 */
public abstract class GenericEntityDataController<P, E extends GenericEntity<I>, I>
    implements CrudController<P, I> {
  protected CustomProperties customProperties;
  protected GenericEntityCrudService<P, E, I> crudService;

  public GenericEntityDataController(CustomProperties customProperties, GenericEntityCrudService<P, E, I> crudService) {
    this.customProperties = customProperties;
    this.crudService = crudService;
  }

  /**
   * Pass a new item to the service.
   * 
   * @return The resulting item's ID
   */
  public I create(P input) {
    I resultId = crudService.create(input);
    return resultId;
  }

  /**
   * Fetch one item by a provided id.
   *
   * @param id The identifier of the item
   * @return The item
   */
  public P readOne(I id) {
    P found = crudService.find(id);
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
    int pageSize = customProperties.getItemsPerPage();
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

  /**
   * Update the item found with an id, using data provided as input.
   * 
   * @param input
   * @param id
   * @return
   */
  public I update(P input, I id) {
    I resultId = crudService.update(input, id);
    return resultId;
  }

  /**
   * Delete an item by its id.
   * 
   * @param id
   * @return
   */
  public boolean delete(I id) {
    boolean result = crudService.delete(id);
    return result;
  }

  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
