package cl.blm.newmarketing.backend.converters.entity2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductTypeDto;
import cl.blm.newmarketing.backend.model.entities.ProductType;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductTypeEntity2Dto
    implements Converter<ProductType, ProductTypeDto> {
  @Override
  public ProductTypeDto convert(ProductType source) {
    ProductTypeDto target = new ProductTypeDto();
    target.setProductTypeId(source.getId());
    target.setProductTypeName(source.getName());
    return target;
  }
}
