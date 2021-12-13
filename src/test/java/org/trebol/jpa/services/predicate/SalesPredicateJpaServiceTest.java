package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.SalesPredicateJpaServiceImpl;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SalesPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("buyOrder", "1").build()), // same as id
                                         service.parseMap(Maps.of("date", Instant.now().toString()).build()),
                                         service.parseMap(Maps.of("statusName", "test").build()),
                                         service.parseMap(Maps.of("token", "test").build()));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size() + 1);
  }

  @Test
  public void only_accepts_correct_dates() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    Predicate whereDateIs = service.parseMap(mapWithDate(Instant.now().toString()));

    assertNotNull(whereDateIs);
    assertNotEquals(whereDateIs, emptyPredicate);

    // invalid date format
    assertEquals(service.parseMap(mapWithDate("1")), emptyPredicate);
    assertEquals(service.parseMap(mapWithDate("10/10")), emptyPredicate);
    assertEquals(service.parseMap(mapWithDate("1984")), emptyPredicate);
    assertEquals(service.parseMap(mapWithDate("!?")), emptyPredicate);
    assertEquals(service.parseMap(mapWithDate("not a date")), emptyPredicate);
  }

  private Map<String, String> mapWithDate(String date) {
    return Maps.of("date", date).build();
  }

  private SalesPredicateJpaServiceImpl instantiate() {
    return new SalesPredicateJpaServiceImpl();
  }
}
