package cl.blm.newmarketing.backend.converters.dto2pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductTypePojo;
import cl.blm.newmarketing.backend.dtos.ProductTypeDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductTypeDto2Pojo
    implements Converter<ProductTypeDto, ProductTypePojo> {
  @Override
  public ProductTypePojo convert(ProductTypeDto source) {
    ProductTypePojo target = new ProductTypePojo();
    target.id = source.getProductTypeId();
    target.name = source.getProductTypeName();
    return target;
  }
}
