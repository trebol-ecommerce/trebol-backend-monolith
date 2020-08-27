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

    if (source.id != null) {
      target.setProductId(source.id);
    }

    if (source.name != null) {
      target.setProductName(source.name);
    }

    if (source.barcode != null) {
      target.setProductCode(source.barcode);
    }

    if (source.price != null) {
      target.setProductPrice(source.price);
    }

    if (source.criticalStock != null) {
      target.setProductStockCritical(source.criticalStock);
    }

    if (source.currentStock != null) {
      target.setProductStockCurrent(source.currentStock);
    }

    return target;
  }
}
