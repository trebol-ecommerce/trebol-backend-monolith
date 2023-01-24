package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.PredicateService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomersPredicateServiceTest {
  private PredicateService<Customer> instance;

  @BeforeEach
  void beforeEach() {
    instance = new CustomersPredicateServiceImpl();
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("idNumber", "id test")),
      instance.parseMap(Map.of("name", "name test")),
      instance.parseMap(Map.of("firstName", "first name test")),
      instance.parseMap(Map.of("lastName", "last name test")),
      instance.parseMap(Map.of("email", "email test")),
      instance.parseMap(Map.of("nameLike", "name portion")),
      instance.parseMap(Map.of("firstNameLike", "first name portion")),
      instance.parseMap(Map.of("lastNameLike", "last name portion")),
      instance.parseMap(Map.of("idNumberLike", "id portion")),
      instance.parseMap(Map.of("emailLike", "email portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
