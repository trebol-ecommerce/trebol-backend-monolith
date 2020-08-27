package cl.blm.newmarketing.backend.converters.entity2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.dtos.ProductDto;
import cl.blm.newmarketing.backend.model.entities.Product;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductEntity2Dto
    implements Converter<Product, ProductDto> {
  @Override
  public ProductDto convert(Product source) {
    ProductDto target = new ProductDto();
    target.setProductId(source.getId());
    target.setProductName(source.getName());
    target.setProductCode(source.getCode());
    target.setProductPrice(source.getPrice());
    target.setProductStockCritical(source.getStockCritical());
    target.setProductStockCurrent(source.getStockCurrent());
    return target;
  }
}
