package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QUser;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class UsersSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<User> {

  public UsersSortSpecJpaServiceImpl() {
    super(Map.of("name", QUser.user.name.asc(),
                 "role", QUser.user.userRole.name.asc()));
  }

  @Override
  public QUser getBasePath() { return QUser.user; }
}
