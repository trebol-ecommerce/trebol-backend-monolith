package cl.blm.newmarketing.store.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.api.pojo.ProductFamilyPojo;
import cl.blm.newmarketing.store.jpa.entities.ProductFamily;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductFamily2Entity
    implements Converter<ProductFamilyPojo, ProductFamily> {

  @Override
  public ProductFamily convert(ProductFamilyPojo source) {
    ProductFamily target = new ProductFamily(source.getId());
    target.setName(source.getName());
    return target;
  }
}
