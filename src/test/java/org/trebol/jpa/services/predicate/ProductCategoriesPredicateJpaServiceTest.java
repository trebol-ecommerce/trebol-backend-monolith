package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.ProductCategoriesPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductCategoriesPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ProductCategoriesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("code", "code test").build()),
                                         service.parseMap(Maps.of("name", "name test").build()),
                                         service.parseMap(Maps.of("nameLike", "name portion").build()),
                                         service.parseMap(Maps.of("parentCode", "parent code test").build()),
                                         service.parseMap(Maps.of("parentId", "1").build()));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ProductCategoriesPredicateJpaServiceImpl instantiate() {
    return new ProductCategoriesPredicateJpaServiceImpl();
  }
}
