package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsPredicateJpaServiceTest {

 @Mock
 IProductCategoryTreeResolver categoryTreeResolver;

  @Test
  void parses_map() {
    when(categoryTreeResolver.getBranchIdsFromRootCode("category-code")).thenReturn(new ArrayList<>());
    Predicate emptyPredicate = new BooleanBuilder();

    ProductsPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("barcode", "test1")),
                                         service.parseMap(Map.of("name", "test2")),
                                         service.parseMap(Map.of("categoryCode", "category-code")),
                                         service.parseMap(Map.of("barcodeLike", "portion")),
                                         service.parseMap(Map.of("nameLike", "portion")),
                                         service.parseMap(Map.of("categoryCodeLike", "portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ProductsPredicateJpaServiceImpl instantiate() {
    return new ProductsPredicateJpaServiceImpl(categoryTreeResolver);
  }
}
