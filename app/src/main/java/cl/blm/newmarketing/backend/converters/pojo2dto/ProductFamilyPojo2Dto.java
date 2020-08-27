package cl.blm.newmarketing.backend.converters.pojo2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductFamilyPojo;
import cl.blm.newmarketing.backend.dtos.ProductFamilyDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductFamilyPojo2Dto
    implements Converter<ProductFamilyPojo, ProductFamilyDto> {
  @Override
  public ProductFamilyDto convert(ProductFamilyPojo source) {
    ProductFamilyDto target = new ProductFamilyDto();

    if (source.id != null) {
      target.setProductFamilyId(source.id);
    }

    if (source.name != null) {
      target.setProductFamilyName(source.name);
    }

    return target;
  }
}
