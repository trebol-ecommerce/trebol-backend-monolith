package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesPredicateJpaServiceTest {
  @Mock
  IProductCategoryTreeResolver treeResolver;

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ProductCategoriesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("code", "code test")),
                                         service.parseMap(Map.of("name", "name test")),
                                         service.parseMap(Map.of("nameLike", "name portion")),
                                         service.parseMap(Map.of("parentCode", "parent code test")),
                                         service.parseMap(Map.of("parentId", "1")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ProductCategoriesPredicateJpaServiceImpl instantiate() {
    return new ProductCategoriesPredicateJpaServiceImpl(treeResolver);
  }
}
