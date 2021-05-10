package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.ProductTypePojo;
import org.trebol.jpa.entities.ProductFamily;
import org.trebol.jpa.entities.ProductType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductType2Entity
    implements Converter<ProductTypePojo, ProductType> {

  @Override
  public ProductType convert(ProductTypePojo source) {
    ProductType target = new ProductType(source.getId());
    target.setName(source.getName());
    target.setProductFamily(new ProductFamily(source.getProductFamily().getId()));
    return target;
  }
}
