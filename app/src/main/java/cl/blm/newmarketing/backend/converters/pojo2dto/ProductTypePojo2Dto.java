package cl.blm.newmarketing.backend.converters.pojo2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductTypePojo;
import cl.blm.newmarketing.backend.dtos.ProductTypeDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductTypePojo2Dto
    implements Converter<ProductTypePojo, ProductTypeDto> {
  @Override
  public ProductTypeDto convert(ProductTypePojo source) {
    ProductTypeDto target = new ProductTypeDto();

    if (source.id != null) {
      target.setProductTypeId(source.id);
    }

    if (source.name != null) {
      target.setProductTypeName(source.name);
    }

    return target;
  }
}
