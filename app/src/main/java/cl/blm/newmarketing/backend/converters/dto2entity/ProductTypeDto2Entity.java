package cl.blm.newmarketing.backend.converters.dto2entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductTypeDto;
import cl.blm.newmarketing.backend.model.entities.ProductType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductTypeDto2Entity
    implements Converter<ProductTypeDto, ProductType> {
  @Override
  public ProductType convert(ProductTypeDto source) {
    ProductType target = new ProductType();
    target.setId(source.getProductTypeId());
    target.setName(source.getProductTypeName());
    return target;
  }
}
