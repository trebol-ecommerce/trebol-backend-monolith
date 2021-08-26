package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductCategory2Entity
    implements Converter<ProductCategoryPojo, ProductCategory> {

  @Override
  public ProductCategory convert(ProductCategoryPojo source) {
    ProductCategory target = new ProductCategory();
    target.setId(source.getId());
    target.setName(source.getName());

    if (source.getParent() != null && source.getParent().getId() != null) {
      ProductCategory targetParent = new ProductCategory();
      targetParent.setId(source.getParent().getId());
      target.setParent(targetParent);
    }
    return target;
  }
}
