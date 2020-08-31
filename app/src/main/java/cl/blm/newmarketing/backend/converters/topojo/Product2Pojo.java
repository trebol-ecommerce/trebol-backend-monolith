package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.api.pojo.ProductTypePojo;
import cl.blm.newmarketing.backend.model.entities.Product;

@Component
public class Product2Pojo
    implements Converter<Product, ProductPojo> {

  @Override
  public ProductPojo convert(Product source) {
    ProductPojo target = new ProductPojo();
    target.id = source.getId();
    target.name = source.getName();
    target.barcode = source.getBarcode();
    target.price = source.getPrice();
    target.currentStock = source.getStockCurrent();
    target.criticalStock = source.getStockCritical();
    target.productType = new ProductTypePojo();
    target.productType.id = source.getProductType().getId();
    return target;
  }
}
