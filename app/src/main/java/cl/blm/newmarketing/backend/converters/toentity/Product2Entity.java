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
    Product target = new Product(source.id);
    target.setName(source.name);
    target.setBarcode(source.barcode);
    target.setPrice(source.price);
    target.setStockCurrent(source.currentStock);
    target.setStockCritical(source.criticalStock);
    target.setProductType(new ProductType(source.productType.id));
    return target;
  }
}
