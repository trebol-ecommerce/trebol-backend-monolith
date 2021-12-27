package org.trebol.jpa.services;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import java.util.Map;

public abstract class GenericSortSpecJpaService<E>
  implements ISortJpaService<E> {

  protected final Map<String, OrderSpecifier<?>> orderSpecifierMap;

  public GenericSortSpecJpaService(Map<String, OrderSpecifier<?>> orderSpecifierMap) {
    this.orderSpecifierMap = orderSpecifierMap;
  }

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    String propertyName = queryParamsMap.get("sortBy");
    OrderSpecifier<?> orderSpecifier = this.orderSpecifierMap.get(propertyName);
    Sort sortBy = QSort.by(orderSpecifier);
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
