package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersPredicateJpaServiceTest {

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    UsersJpaPredicateServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("name", "test")),
                                         service.parseMap(Map.of("email", "test")),
                                         service.parseMap(Map.of("nameLike", "name portion")),
                                         service.parseMap(Map.of("emailLike", "email portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private UsersJpaPredicateServiceImpl instantiate() {
    return new UsersJpaPredicateServiceImpl();
  }
}
