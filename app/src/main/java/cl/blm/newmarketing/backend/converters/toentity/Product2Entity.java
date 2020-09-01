package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.model.entities.ProductType;

@Component
public class Product2Entity
    implements Converter<ProductPojo, Product> {

  @Override
  public Product convert(ProductPojo source) {
    Product target = new Product(source.getId());
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    target.setStockCurrent(source.getCurrentStock());
    target.setStockCritical(source.getCriticalStock());
    target.setProductType(new ProductType(source.getProductType().getId()));
    return target;
  }
}
