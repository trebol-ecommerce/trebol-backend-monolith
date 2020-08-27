package cl.blm.newmarketing.backend.converters.dto2pojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojos.ProductPojo;
import cl.blm.newmarketing.backend.dtos.ProductDto;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class ProductDto2Pojo
    implements Converter<ProductDto, ProductPojo> {
  @Override
  public ProductPojo convert(ProductDto source) {
    ProductPojo target = new ProductPojo();
    target.id = source.getProductId();
    target.name = source.getProductName();
    target.barcode = source.getProductCode();
    target.price = source.getProductPrice();
    target.criticalStock = source.getProductStockCritical();
    target.currentStock = source.getProductStockCurrent();
    return target;
  }
}
