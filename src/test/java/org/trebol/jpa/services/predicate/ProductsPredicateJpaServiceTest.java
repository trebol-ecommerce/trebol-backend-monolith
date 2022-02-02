package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.trebol.jpa.services.IProductCategoryTreeResolver;
import org.trebol.jpa.services.predicates.ProductsPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductsPredicateJpaServiceTest {

 @Mock
 IProductCategoryTreeResolver categoryTreeResolver;

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ProductsPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("barcode", "barcode test")),
                                         service.parseMap(Map.of("name", "name test")),
                                         service.parseMap(Map.of("barcodeLike", "barcode portion")),
                                         service.parseMap(Map.of("nameLike", "name portion")),
                                         service.parseMap(Map.of("productCategory", "category name")),
                                         service.parseMap(Map.of("productCategoryLike", "category name portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ProductsPredicateJpaServiceImpl instantiate() {
    return new ProductsPredicateJpaServiceImpl(categoryTreeResolver);
  }
}
