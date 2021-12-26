package org.trebol.jpa.services.sortspecs;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.QCustomer;
import org.trebol.jpa.services.ISortJpaService;

import java.util.Map;

@Service
public class CustomersSortSpecJpaServiceImpl
  implements ISortJpaService<Customer> {
  @Override
  public QCustomer getBasePath() { return QCustomer.customer; }

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    Sort sortBy;
    switch (queryParamsMap.get("sortBy")) {
      case "idNumber": sortBy = QSort.by(getBasePath().person.idNumber.asc()); break;
      case "firstName": sortBy = QSort.by(getBasePath().person.firstName.asc()); break;
      case "email": sortBy = QSort.by(getBasePath().person.email.asc()); break;
      case "phone1": sortBy = QSort.by(getBasePath().person.phone1.asc()); break;
      case "phone2": sortBy = QSort.by(getBasePath().person.phone2.asc()); break;
      case "name":
      case "lastName": sortBy = QSort.by(getBasePath().person.lastName.asc()); break;
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
