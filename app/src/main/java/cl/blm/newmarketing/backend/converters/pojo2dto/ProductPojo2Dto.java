package cl.blm.newmarketing.backend.converters.pojo2dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductPojo;
import cl.blm.newmarketing.backend.dtos.ProductDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductPojo2Dto
    implements Converter<ProductPojo, ProductDto> {
  @Override
  public ProductDto convert(ProductPojo source) {
    ProductDto target = new ProductDto();
    target.setProductId(source.id);
    target.setProductName(source.name);
    target.setProductCode(source.barcode);
    target.setProductPrice(source.price);
    target.setProductStockCritical(source.criticalStock);
    target.setProductStockCurrent(source.currentStock);
    return target;
  }
}
