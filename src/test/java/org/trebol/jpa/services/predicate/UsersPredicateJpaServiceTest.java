package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.UsersJpaPredicateServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    UsersJpaPredicateServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("name", "test").build()),
                                         service.parseMap(Maps.of("email", "test").build()),
                                         service.parseMap(Maps.of("nameLike", "name portion").build()),
                                         service.parseMap(Maps.of("emailLike", "email portion").build()));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private UsersJpaPredicateServiceImpl instantiate() {
    return new UsersJpaPredicateServiceImpl();
  }
}
