package cl.blm.trebol.services.exposed;

import java.util.Collection;
import java.util.Map;

import cl.blm.trebol.api.pojo.ProductFamilyPojo;
import cl.blm.trebol.api.pojo.ProductPojo;
import cl.blm.trebol.api.pojo.ProductTypePojo;

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
