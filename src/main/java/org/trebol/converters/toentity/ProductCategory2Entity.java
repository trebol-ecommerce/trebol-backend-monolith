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
    if (source.getCode() != null) {
      target.setId(source.getCode());
    }
    target.setName(source.getName());
    return target;
  }
}
