package org.trebol.operation;

import java.util.Map;

public interface PaginationService {
  int determineRequestedPageIndex(Map<String, String> requestParams)
    throws NumberFormatException;

  int determineRequestedPageSize(Map<String, String> requestParams)
    throws NumberFormatException;
}
