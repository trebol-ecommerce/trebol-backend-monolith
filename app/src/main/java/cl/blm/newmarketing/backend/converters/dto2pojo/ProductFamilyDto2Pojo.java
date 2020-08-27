package cl.blm.newmarketing.backend.converters.dto2pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductFamilyPojo;
import cl.blm.newmarketing.backend.dtos.ProductFamilyDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductFamilyDto2Pojo
    implements Converter<ProductFamilyDto, ProductFamilyPojo> {
  @Override
  public ProductFamilyPojo convert(ProductFamilyDto source) {
    ProductFamilyPojo target = new ProductFamilyPojo();
    target.id = source.getProductFamilyId();
    target.name = source.getProductFamilyName();
    return target;
  }
}
