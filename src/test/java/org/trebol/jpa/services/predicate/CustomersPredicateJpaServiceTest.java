package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.CustomersPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomersPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    CustomersPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("idNumber", "id test").build()),
                                         service.parseMap(Maps.of("name", "name test").build()),
                                         service.parseMap(Maps.of("firstName", "first name test").build()),
                                         service.parseMap(Maps.of("lastName", "last name test").build()),
                                         service.parseMap(Maps.of("email", "email test").build()),
                                         service.parseMap(Maps.of("nameLike", "name portion").build()),
                                         service.parseMap(Maps.of("firstNameLike", "first name portion").build()),
                                         service.parseMap(Maps.of("lastNameLike", "last name portion").build()),
                                         service.parseMap(Maps.of("idNumberLike", "id portion").build()),
                                         service.parseMap(Maps.of("emailLike", "email portion").build()));
    Set<Predicate> predicateSet = new HashSet<>(predicates);
    assertEquals(predicates.size(), predicateSet.size());
  }

  private CustomersPredicateJpaServiceImpl instantiate() {
    return new CustomersPredicateJpaServiceImpl();
  }
}
