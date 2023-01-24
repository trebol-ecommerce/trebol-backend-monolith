package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.services.PredicateService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingCompaniesPredicateServiceTest {
  private PredicateService<BillingCompany> instance;

  @BeforeEach
  void beforeEach() {
    instance = new BillingCompaniesPredicateServiceImpl();
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("idNumber", "id test")),
      instance.parseMap(Map.of("name", "name test")),
      instance.parseMap(Map.of("idNumberLike", "id number portion")),
      instance.parseMap(Map.of("nameLike", "name portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
