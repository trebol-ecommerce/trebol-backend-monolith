package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductTypePojo;
import cl.blm.newmarketing.backend.jpa.entities.ProductType;

@Component
public class ProductType2Pojo
    implements Converter<ProductType, ProductTypePojo> {

  @Override
  public ProductTypePojo convert(ProductType source) {
    ProductTypePojo target = new ProductTypePojo();
    target.setId(source.getId());
    target.setName(source.getName());
    return target;
  }
}
