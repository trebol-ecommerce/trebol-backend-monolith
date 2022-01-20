/*
 * Copyright (c) 2022 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.operation;

import org.springframework.data.domain.Sort;
import org.trebol.config.OperationProperties;

import java.util.Map;

/**
 * RestController that implements IDataController with a GenericJpaService
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
