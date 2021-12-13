package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.ProductsPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductsPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ProductsPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("barcode", "barcode test").build()),
                                         service.parseMap(Maps.of("name", "name test").build()),
                                         service.parseMap(Maps.of("barcodeLike", "barcode portion").build()),
                                         service.parseMap(Maps.of("nameLike", "name portion").build()),
                                         service.parseMap(Maps.of("productCategory", "category name").build()),
                                         service.parseMap(Maps.of("productCategoryLike", "category name portion").build()));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ProductsPredicateJpaServiceImpl instantiate() {
    return new ProductsPredicateJpaServiceImpl();
  }
}
