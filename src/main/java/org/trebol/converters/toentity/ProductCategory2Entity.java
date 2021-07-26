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
    ProductCategory target = new ProductCategory(source.getId());
    target.setName(source.getName());
    target.setParent(new ProductCategory(source.getParent().getId()));
    return target;
  }
}
