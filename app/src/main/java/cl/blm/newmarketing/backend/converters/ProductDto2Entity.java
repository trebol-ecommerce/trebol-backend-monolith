package cl.blm.newmarketing.backend.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.model.entities.Product;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductDto2Entity
    implements Converter<ProductDto, Product> {
  @Override
  public Product convert(ProductDto source) {
    Product target = new Product();
    target.setId(source.getProductId());
    target.setName(source.getProductName());
    target.setCode(source.getProductCode());
    target.setPrice(source.getProductPrice());
    target.setStockCritical(source.getProductStockCritical());
    target.setStockCurrent(source.getProductStockCurrent());
    return target;
  }
}
