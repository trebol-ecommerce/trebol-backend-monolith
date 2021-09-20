package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.ProductCategory;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductCategory2Pojo
    implements Converter<ProductCategory, ProductCategoryPojo> {

  @Override
  public ProductCategoryPojo convert(ProductCategory source) {
    ProductCategoryPojo target = new ProductCategoryPojo();
    target.setCode(source.getId());
    target.setName(source.getName());
    return target;
  }
}
