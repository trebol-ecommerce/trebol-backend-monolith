package org.trebol.api;

import java.util.Collection;

import org.trebol.api.pojo.ProductCategoryPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IProductCategoriesService {
  public Collection<ProductCategoryPojo> getRootCategories();
  public Collection<ProductCategoryPojo> getChildrenCategories(long parentId);
}
