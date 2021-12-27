package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.QProduct;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class ProductsSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<Product> {

  public ProductsSortSpecJpaServiceImpl() {
    super(Map.of("name", QProduct.product.name.asc(),
                 "barcode", QProduct.product.barcode.asc(),
                 "price", QProduct.product.price.asc(),
                 "category", QProduct.product.productCategory.name.asc()));
  }

  @Override
  public QProduct getBasePath() { return QProduct.product; }
}
