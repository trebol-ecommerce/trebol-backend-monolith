package org.trebol.jpa.services.sortspecs;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QUser;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.ISortJpaService;

import java.util.Map;

@Service
public class UsersSortSpecJpaServiceImpl
  implements ISortJpaService<User> {
  @Override
  public QUser getBasePath() { return QUser.user; }

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    Sort sortBy;
    switch (queryParamsMap.get("sortBy")) {
      case "name": sortBy = QSort.by(getBasePath().name.asc()); break;
      case "role": sortBy = QSort.by(getBasePath().userRole.name.asc()); break;
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
