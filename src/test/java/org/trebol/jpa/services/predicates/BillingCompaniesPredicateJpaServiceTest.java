package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingCompaniesPredicateJpaServiceTest {

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    BillingCompaniesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("idNumber", "id test")),
                                         service.parseMap(Map.of("name", "name test")),
                                         service.parseMap(Map.of("idNumberLike", "id number portion")),
                                         service.parseMap(Map.of("nameLike", "name portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private BillingCompaniesPredicateJpaServiceImpl instantiate() {
    return new BillingCompaniesPredicateJpaServiceImpl();
  }
}
