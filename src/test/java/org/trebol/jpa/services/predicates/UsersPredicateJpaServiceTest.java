package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersPredicateJpaServiceTest {
  private IPredicateJpaService<User> instance;

  @BeforeEach
  void beforeEach() {
    instance = new UsersJpaPredicateServiceImpl();
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("name", "test")),
      instance.parseMap(Map.of("email", "test")),
      instance.parseMap(Map.of("nameLike", "name portion")),
      instance.parseMap(Map.of("emailLike", "email portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
