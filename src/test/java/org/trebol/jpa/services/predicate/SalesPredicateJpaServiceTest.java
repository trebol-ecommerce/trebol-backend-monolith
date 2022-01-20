package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.services.predicates.SalesPredicateJpaServiceImpl;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SalesPredicateJpaServiceTest {

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("buyOrder", "1")), // same as id
                                         service.parseMap(Map.of("date", Instant.now().toString())),
                                         service.parseMap(Map.of("statusName", "test")),
                                         service.parseMap(Map.of("token", "test")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size() + 1);
  }

  @Test
  void only_accepts_correct_dates() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    Predicate whereDateIs = service.parseMap(Map.of("date", Instant.now().toString()));

    assertNotNull(whereDateIs);
    assertNotEquals(whereDateIs, emptyPredicate);

    // invalid date format
    assertEquals(service.parseMap(Map.of("date", "1")), emptyPredicate);
    assertEquals(service.parseMap(Map.of("date", "10/10")), emptyPredicate);
    assertEquals(service.parseMap(Map.of("date", "1984")), emptyPredicate);
    assertEquals(service.parseMap(Map.of("date", "!?")), emptyPredicate);
    assertEquals(service.parseMap(Map.of("date", "not a date")), emptyPredicate);
  }

  private SalesPredicateJpaServiceImpl instantiate() {
    return new SalesPredicateJpaServiceImpl();
  }
}
