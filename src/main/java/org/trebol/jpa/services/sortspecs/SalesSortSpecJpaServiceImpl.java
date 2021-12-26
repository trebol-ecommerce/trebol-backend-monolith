package org.trebol.jpa.services.sortspecs;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QSell;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.ISortJpaService;

import java.util.Map;

@Service
public class SalesSortSpecJpaServiceImpl
  implements ISortJpaService<Sell> {
  @Override
  public QSell getBasePath() { return QSell.sell; }

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    Sort sortBy;
    switch (queryParamsMap.get("sortBy")) {
      case "buyOrder": sortBy = QSort.by(getBasePath().id.asc()); break;
      case "date": sortBy = QSort.by(getBasePath().date.asc()); break;
      case "status": sortBy = QSort.by(getBasePath().status.code.asc()); break;
      case "customer": sortBy = QSort.by(getBasePath().customer.person.lastName.asc()); break;
      case "shipper": sortBy = QSort.by(getBasePath().shipper.name.asc()); break;
      case "totalValue": sortBy = QSort.by(getBasePath().totalValue.asc()); break;
      case "netValue": sortBy = QSort.by(getBasePath().netValue.asc()); break;
      case "totalItems": sortBy = QSort.by(getBasePath().totalItems.asc()); break;
      case "transportValue": sortBy = QSort.by(getBasePath().transportValue.asc()); break;
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
