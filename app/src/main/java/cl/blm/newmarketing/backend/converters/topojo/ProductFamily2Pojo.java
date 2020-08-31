package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductFamilyPojo;
import cl.blm.newmarketing.backend.model.entities.ProductFamily;

@Component
public class ProductFamily2Pojo
    implements Converter<ProductFamily, ProductFamilyPojo> {

  @Override
  public ProductFamilyPojo convert(ProductFamily source) {
    ProductFamilyPojo target = new ProductFamilyPojo();
    target.id = source.getId();
    target.name = source.getName();
    return target;
  }
}
