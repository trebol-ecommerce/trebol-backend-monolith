package org.trebol.operation;

import org.trebol.pojo.DataPagePojo;

import java.util.Map;

/**
 * Interface for API controllers that handle reading of data stored in a persistence context and
 * return it converted to the parameterized class, presumably a Pojo.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 */
public interface IDataController<P> {

  /**
   * Get a paged collection of Pojos.
   * @param pageSize The maximum amount of items in the returned page, may differ from the actual amount.
   *                 If null, defaults to the value of the property `application.custom.itemsPerPage`.
   * @param pageIndex The desired 0-based index of the page. If null, defaults to 0.
   * @param requestParams A map of key/value String pairs, that may be interpreted as filtering conditions. Can be null.
   * @return An instance of DataPagePojo, with the items and page information.
   */
  DataPagePojo<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);
}
