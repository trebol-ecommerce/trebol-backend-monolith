package org.trebol.operation;

import org.trebol.pojo.DataPagePojo;

import java.util.Map;

/**
 * Interface for API controllers that handle CREATE and READ requests.
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <P> The Pojo class
 */
public interface IDataController<P> {

  DataPagePojo<P> readMany(Integer pageSize, Integer pageIndex, Map<String, String> requestParams);
}
