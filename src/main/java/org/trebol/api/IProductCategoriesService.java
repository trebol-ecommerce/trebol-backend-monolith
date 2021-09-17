package org.trebol.api;

import java.util.Collection;

import org.trebol.pojo.ProductCategoryPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface IProductCategoriesService {
  Collection<ProductCategoryPojo> getRootCategories();
  Collection<ProductCategoryPojo> getChildrenCategories(long parentId);
}
