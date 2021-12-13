package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.SalesPredicateJpaServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    Predicate whereIdIs = service.parseMap(Maps.of("id", "1").build());
    Predicate whereNameIs = service.parseMap(Maps.of("name", "test").build());
    Predicate whereEmailIs = service.parseMap(Maps.of("email", "test").build());
    Predicate whereNameIsLike = service.parseMap(Maps.of("nameLike", "name portion").build());
    Predicate whereEmailIsLike = service.parseMap(Maps.of("emailLike", "email portion").build());

    assertNotNull(whereIdIs);
    assertNotNull(whereNameIs);
    assertNotNull(whereEmailIs);
    assertNotNull(whereNameIsLike);
    assertNotNull(whereEmailIsLike);
    assertNotEquals(whereIdIs, emptyPredicate);
    assertNotEquals(whereNameIs, emptyPredicate);
    assertNotEquals(whereEmailIs, emptyPredicate);
    assertNotEquals(whereNameIsLike, emptyPredicate);
    assertNotEquals(whereEmailIsLike, emptyPredicate);

    assertNotEquals(whereIdIs, whereNameIs);
    assertNotEquals(whereIdIs, whereEmailIs);
    assertNotEquals(whereIdIs, whereNameIsLike);
    assertNotEquals(whereIdIs, whereEmailIsLike);
    assertNotEquals(whereNameIs, whereEmailIs);
    assertNotEquals(whereNameIs, whereNameIsLike);
    assertNotEquals(whereEmailIs, whereEmailIsLike);
  }

  private SalesPredicateJpaServiceImpl instantiate() {
    return new SalesPredicateJpaServiceImpl();
  }
}
