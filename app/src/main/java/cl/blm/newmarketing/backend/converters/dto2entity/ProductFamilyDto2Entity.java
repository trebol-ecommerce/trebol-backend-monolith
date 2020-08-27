package cl.blm.newmarketing.backend.converters.dto2entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductFamilyDto;
import cl.blm.newmarketing.backend.model.entities.ProductFamily;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductFamilyDto2Entity
    implements Converter<ProductFamilyDto, ProductFamily> {
  @Override
  public ProductFamily convert(ProductFamilyDto source) {
    ProductFamily target = new ProductFamily();
    target.setId(source.getProductFamilyId());
    target.setName(source.getProductFamilyName());
    return target;
  }
}
