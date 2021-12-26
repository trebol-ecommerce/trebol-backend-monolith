package org.trebol.jpa.services.sortspecs;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.QProduct;
import org.trebol.jpa.services.ISortJpaService;

import java.util.Map;

@Service
public class ProductsSortSpecJpaServiceImpl
  implements ISortJpaService<Product> {
  @Override
  public QProduct getBasePath() { return QProduct.product; }

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    Sort sortBy;
    switch (queryParamsMap.get("sortBy")) {
      case "name": sortBy = QSort.by(getBasePath().name.asc()); break;
      case "barcode": sortBy = QSort.by(getBasePath().barcode.asc()); break;
      case "price": sortBy = QSort.by(getBasePath().price.asc()); break;
      case "category": sortBy = QSort.by(getBasePath().productCategory.name.asc()); break;
      default: return null;
    }
    switch (queryParamsMap.get("order")) {
      case "asc":
        return sortBy.ascending();
      case "desc":
        return sortBy.descending();
      default:
        return sortBy;
    }
  }
}
