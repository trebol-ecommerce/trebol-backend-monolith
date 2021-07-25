package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Product2Entity
    implements Converter<ProductPojo, Product> {

  @Override
  public Product convert(ProductPojo source) {
    Product target = new Product(source.getId());
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    target.setProductCategory(new ProductCategory(source.getProductType().getId()));

    if (source.getCurrentStock() != null) {
      target.setStockCurrent(source.getCurrentStock());
    }

    if (source.getCriticalStock() != null) {
      target.setStockCritical(source.getCriticalStock());
    }
    return target;
  }
}
