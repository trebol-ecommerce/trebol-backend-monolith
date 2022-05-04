package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomersPredicateJpaServiceTest {

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    CustomersPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("idNumber", "id test")),
                                         service.parseMap(Map.of("name", "name test")),
                                         service.parseMap(Map.of("firstName", "first name test")),
                                         service.parseMap(Map.of("lastName", "last name test")),
                                         service.parseMap(Map.of("email", "email test")),
                                         service.parseMap(Map.of("nameLike", "name portion")),
                                         service.parseMap(Map.of("firstNameLike", "first name portion")),
                                         service.parseMap(Map.of("lastNameLike", "last name portion")),
                                         service.parseMap(Map.of("idNumberLike", "id portion")),
                                         service.parseMap(Map.of("emailLike", "email portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private CustomersPredicateJpaServiceImpl instantiate() {
    return new CustomersPredicateJpaServiceImpl();
  }
}
