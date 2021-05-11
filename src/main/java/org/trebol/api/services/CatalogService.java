package org.trebol.api.services;

import java.util.Collection;
import java.util.Map;

import org.trebol.api.pojo.ProductFamilyPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.ProductTypePojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CatalogService {
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex,
      Map<String, String> allRequestParams);

  public ProductPojo readProduct(Integer id);

  public Collection<ProductTypePojo> readProductTypes();

  public Collection<ProductTypePojo> readProductTypesByFamilyId(int productFamilyId);

  public Collection<ProductFamilyPojo> readProductFamilies();
}
