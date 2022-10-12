package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.services.IPredicateJpaService;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SalesPredicateJpaServiceTest {
  private IPredicateJpaService<Sell> instance;

  @BeforeEach
  void beforeEach() {
    instance = new SalesPredicateJpaServiceImpl();
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("buyOrder", "1")), // same as id
      instance.parseMap(Map.of("date", Instant.now().toString())),
      instance.parseMap(Map.of("statusName", "test")),
      instance.parseMap(Map.of("token", "test")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size() + 1);
  }

  @Test
  void only_accepts_correct_dates() {
    Predicate emptyPredicate = new BooleanBuilder();
    Predicate whereDateIs = instance.parseMap(Map.of("date", Instant.now().toString()));

    assertNotNull(whereDateIs);
    assertNotEquals(whereDateIs, emptyPredicate);

    // invalid date format
    assertEquals(instance.parseMap(Map.of("date", "1")), emptyPredicate);
    assertEquals(instance.parseMap(Map.of("date", "10/10")), emptyPredicate);
    assertEquals(instance.parseMap(Map.of("date", "1984")), emptyPredicate);
    assertEquals(instance.parseMap(Map.of("date", "!?")), emptyPredicate);
    assertEquals(instance.parseMap(Map.of("date", "not a date")), emptyPredicate);
  }
}
