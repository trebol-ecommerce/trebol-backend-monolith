package cl.blm.newmarketing.backend.converters.entity2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductFamilyDto;
import cl.blm.newmarketing.backend.model.entities.ProductFamily;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductFamilyEntity2Dto
    implements Converter<ProductFamily, ProductFamilyDto> {
  @Override
  public ProductFamilyDto convert(ProductFamily source) {
    ProductFamilyDto target = new ProductFamilyDto();
    target.setProductFamilyId(source.getId());
    target.setProductFamilyName(source.getName());
    return target;
  }
}
