package org.trebol.operation;

import org.springframework.data.domain.Sort;
import org.trebol.config.OperationProperties;

import java.util.Map;

/**
 * RestController that implements IDataController with a GenericJpaService.
 */
public abstract class GenericPaginationController {

  protected final OperationProperties operationProperties;

  public GenericPaginationController(OperationProperties operationProperties) {
    this.operationProperties = operationProperties;
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

  protected int determineRequestedPageIndex(Map<String, String> requestParams)
      throws NumberFormatException {
    if (requestParams != null && requestParams.containsKey("pageIndex")) {
      return Integer.parseInt(requestParams.get("pageIndex"));
    }
    return 0;
  }

  protected int determineRequestedPageSize(Map<String, String> requestParams)
      throws NumberFormatException {
    if (requestParams != null && requestParams.containsKey("pageSize")) {
      int pageSize = Integer.parseInt(requestParams.get("pageSize"));
      Integer maxAllowedPageSize = operationProperties.getMaxAllowedPageSize();
      return (pageSize < maxAllowedPageSize) ?
          pageSize :
          maxAllowedPageSize;
    }
    return operationProperties.getItemsPerPage();
  }
}
