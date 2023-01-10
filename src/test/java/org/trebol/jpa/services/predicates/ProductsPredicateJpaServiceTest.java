package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsPredicateJpaServiceTest {
  @Mock IProductCategoryTreeResolver categoryTreeResolver;
  IPredicateJpaService<Product> instance;

  @BeforeEach
  void beforeEach() {
    instance = new ProductsPredicateJpaServiceImpl(categoryTreeResolver);
  }

  @Test
  void parses_map() {
    when(categoryTreeResolver.getBranchIdsFromRootCode("category-code")).thenReturn(new ArrayList<>());
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         instance.parseMap(Map.of("id", "1")),
                                         instance.parseMap(Map.of("barcode", "test1")),
                                         instance.parseMap(Map.of("name", "test2")),
                                         instance.parseMap(Map.of("categoryCode", "category-code")),
                                         instance.parseMap(Map.of("barcodeLike", "portion")),
                                         instance.parseMap(Map.of("nameLike", "portion")),
                                         instance.parseMap(Map.of("categoryCodeLike", "portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
