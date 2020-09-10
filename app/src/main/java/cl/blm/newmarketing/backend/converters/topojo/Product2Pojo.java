package cl.blm.newmarketing.backend.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.jpa.entities.Product;

@Component
public class Product2Pojo
    implements Converter<Product, ProductPojo> {

  @Override
  public ProductPojo convert(Product source) {
    ProductPojo target = new ProductPojo();
    target.setId(source.getId());
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    target.setCurrentStock(source.getStockCurrent());
    target.setCriticalStock(source.getStockCritical());
    return target;
  }
}
