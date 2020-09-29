package cl.blm.newmarketing.store.services.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Service;

import cl.blm.newmarketing.store.api.pojo.ProductFamilyPojo;
import cl.blm.newmarketing.store.api.pojo.ProductPojo;
import cl.blm.newmarketing.store.api.pojo.ProductTypePojo;
import cl.blm.newmarketing.store.services.CatalogService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@Service
public class CatalogServiceImpl
    implements CatalogService {

  @Override
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex, Map<String, String> allRequestParams) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public ProductPojo readProduct(Integer id) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Collection<ProductTypePojo> readProductTypes() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Collection<ProductFamilyPojo> readProductFamilies() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
