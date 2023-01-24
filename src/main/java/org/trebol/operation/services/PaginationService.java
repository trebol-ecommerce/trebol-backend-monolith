package org.trebol.operation.services;

import java.util.Map;

public interface PaginationService {
  int determineRequestedPageIndex(Map<String, String> requestParams)
    throws NumberFormatException;

  int determineRequestedPageSize(Map<String, String> requestParams)
    throws NumberFormatException;
}
