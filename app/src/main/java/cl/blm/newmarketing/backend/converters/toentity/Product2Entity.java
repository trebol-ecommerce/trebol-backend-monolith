package cl.blm.newmarketing.backend.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.jpa.entities.Product;
import cl.blm.newmarketing.backend.jpa.entities.ProductType;

@Component
public class Product2Entity
    implements Converter<ProductPojo, Product> {

  @Override
  public Product convert(ProductPojo source) {
    Product target = new Product(source.getId());
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    target.setProductType(new ProductType(source.getProductType().getId()));

    if (source.getCurrentStock() != null) {
      target.setStockCurrent(source.getCurrentStock());
    }

    if (source.getCriticalStock() != null) {
      target.setStockCritical(source.getCriticalStock());
    }
    return target;
  }
}
