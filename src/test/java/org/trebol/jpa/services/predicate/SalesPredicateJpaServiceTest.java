package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.SalesPredicateJpaServiceImpl;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SalesPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    SalesPredicateJpaServiceImpl service = instantiate();
    Predicate whereIdIs = service.parseMap(Maps.of("id", "1").build());
    Predicate whereBuyOrderIs = service.parseMap(Maps.of("buyOrder", "1").build());
    Predicate whereDateIs = service.parseMap(Maps.of("date", Instant.now().toString()).build());
    Predicate whereStatusNameIs = service.parseMap(Maps.of("statusName", "test").build());
    Predicate whereTokenIs = service.parseMap(Maps.of("token", "test").build());

    assertNotNull(whereIdIs);
    assertNotNull(whereBuyOrderIs);
    assertNotNull(whereDateIs);
    assertNotNull(whereStatusNameIs);
    assertNotNull(whereTokenIs);
    assertNotEquals(whereIdIs, emptyPredicate);
    assertNotEquals(whereBuyOrderIs, emptyPredicate);
    assertNotEquals(whereDateIs, emptyPredicate);
    assertNotEquals(whereStatusNameIs, emptyPredicate);
    assertNotEquals(whereTokenIs, emptyPredicate);

    assertEquals(whereIdIs, whereBuyOrderIs);
    assertNotEquals(whereIdIs, whereDateIs);
    assertNotEquals(whereIdIs, whereStatusNameIs);
    assertNotEquals(whereIdIs, whereTokenIs);
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
