package org.trebol.api.services;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.ProductCategoryPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface PublicProductsService {
  public Collection<ProductPojo> readProducts(Integer requestPageSize, Integer requestPageIndex,
      Map<String, String> allRequestParams);

  @Nullable
  public ProductPojo getProduct(Integer id);

  public Collection<ProductCategoryPojo> getRootCategories();

  public Collection<ProductCategoryPojo> getChildrenCategories(int parentId);
}
